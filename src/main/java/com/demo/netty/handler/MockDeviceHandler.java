package com.demo.netty.handler;

import com.demo.netty.entity.MockDevice;
import com.demo.netty.entity.RequestType;
import com.demo.netty.server.MockClient;
import com.demo.netty.util.MessageBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Data
@Slf4j
public class MockDeviceHandler extends ChannelInboundHandlerAdapter {
    private MockDevice device;
    private String remoteIp;
    private int remotePort;

    public MockDeviceHandler(MockDevice device,String ip,int port) {
        this.device = device;
        this.remoteIp = ip;
        this.remotePort = port;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = null;
        if (device.isAgFinish()) {
            msg = MessageBuilder.buildAgAsMsg(RequestType.AS,device);
            log.info("登录 ↑↑↑：{}, imei: {}", msg, device.getImei());
        }else {
            msg = MessageBuilder.buildAgAsMsg(RequestType.AG,device);
            log.info("寻址 ↑↑↑：{}, imei: {}", msg, device.getImei());
        }
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("与平台断连");
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String serverMsg = msg.toString();
        log.info("SERVER ↓↓↓: {}, imei: {}", serverMsg, device.getImei());
        String resp = null;
        if(StringUtils.isNotBlank(serverMsg)){
            if (StringUtils.countMatches(serverMsg,"|a2|621")>0) {
                String ip = StringUtils.substringBetween(serverMsg, "|621,", ",");
                String portTemp = StringUtils.substringAfter(serverMsg,ip + ",");
                int port = 0;
                if (StringUtils.isNotBlank(portTemp)) {
                    String portHex = tailTrim(portTemp);
                    port = Integer.valueOf(portHex,16);
                }
                device.setAgFinish(true);
                //收到登录服务器地址，如果地址一样，则直接登录，不一样须重新建连
                if (StringUtils.equals(ip,remoteIp) && remotePort == port) {
                   channelActive(ctx);
                }else{
                    MockClient client = new MockClient(device,ip,port);
                    client.connect();
                }
            }else if(StringUtils.countMatches(serverMsg,"|a4|")>0){
                log.info("登录成功！");
                resp = MessageBuilder.buildRespMsg(RequestType.PUBLISH,device,"10c");
            }else {
                String[] units = StringUtils.split(serverMsg,"|");
                if(units.length<3){
                    return;
                }
                String function = units[1];
                String content = units[2];
                String[] tagNparas = StringUtils.split(content,",");
                String tag = tagNparas[0];
                String value = null;
                value = buildMultiParaValue(tagNparas,false);
                if(!MessageBuilder.isSupportedTag(device,tag)){
                    //不支持的标签
                    resp = MessageBuilder.buildNotSupportResp(tag);
                }else if(StringUtils.equals(RequestType.QUERY.getFunction(),function)){
                    //查询
                    resp = MessageBuilder.buildRespMsg(RequestType.QUERY_ACK,device,tag);
                }else if(StringUtils.equals(RequestType.SETTING.getFunction(),function)){
                    //设置
                    resp = StringUtils.replace(serverMsg,"|3|","|4|");
                    if(StringUtils.countMatches(serverMsg,"1*")==0){
                        resp = StringUtils.replace(resp,"*","1*");
                    }
                    resp = MessageBuilder.outQuote(resp);
                    MessageBuilder.deviceInfoSetting(device,tag,value);
                }else if(StringUtils.equals(RequestType.WRITE.getFunction(),function)){
                    //透传设置
                    if(StringUtils.equals("613", tag)){
                        String[] bParas = StringUtils.split(value,",");
                        if(bParas.length<2){
                            log.error("613透传消息格式不对，msg = {}", serverMsg);
                            return;
                        }
                        String attachId = bParas[0];
                        String bFuncNtag = bParas[1];
                        String[] bFuncNtagArr = StringUtils.split(bFuncNtag,"#");
                        String bFunc = bFuncNtagArr[0];
                        String bTag = bFuncNtagArr[1];
                        String bValue = buildMultiParaValue(bParas,true);
                        if(StringUtils.equals(RequestType.SETTING.getFunction(),bFunc)){
                            resp = StringUtils.replace(serverMsg,"5|613","5|614");
                            resp = StringUtils.replace(resp,"3#b","4#b");
                            if(StringUtils.countMatches(serverMsg,"1*")==0){
                                resp = StringUtils.replace(resp,"*","1*");
                            }
                            resp = MessageBuilder.outQuote(resp);
                        }
                    }
                }else if(StringUtils.equals(RequestType.PUBLISH_ACK.getFunction(),function)){
                    //平台的ACK，可以不做任何处理
                }else {
                    log.error("暂时不支持的消息类型：msg = {}",serverMsg);
                }
            }

            if(StringUtils.isNotBlank(resp)){
                log.info("回复 ↑↑↑：{}, imei: {}", resp, device.getImei());
                ctx.writeAndFlush(resp);
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.equals(IdleStateEvent.WRITER_IDLE_STATE_EVENT)){
            String heatbeat = "()";
            log.info("心跳 ↑↑↑：{}, imei: {}", heatbeat, device.getImei());
            ctx.channel().writeAndFlush(heatbeat);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("MockDeviceHandler处理发生异常：{}",cause);
        ctx.close();
    }



    private String tailTrim(String tempData) {
        if (tempData.endsWith(",")) {
            tempData = tempData.substring(0, tempData.length() - 1);
        }
        if (tempData.endsWith(",|")) {
            tempData = tempData.substring(0, tempData.length() - 2);
        }
        if (tempData.endsWith("|")) {
            tempData = tempData.substring(0, tempData.length() - 1);
        }
        if (tempData.endsWith(",|)")) {
            tempData = tempData.substring(0, tempData.length() - 3);
        }
        if (tempData.endsWith("|)")) {
            tempData = tempData.substring(0, tempData.length() - 2);
        }

        return tempData;
    }

    private String buildMultiParaValue(String[] tagNparas,boolean isTransmit){
        if(tagNparas.length<2 && !isTransmit){
            return "";
        }else if(tagNparas.length<3 && isTransmit){
            return "";
        }else {
            int i = 1;
            if(isTransmit){
                i = 2;
            }
            StringBuilder sb = new StringBuilder();
            for(;i<tagNparas.length;i++){
                String para = tagNparas[i];
                if(i==tagNparas.length-1){
                    para = tailTrim(para);
                }
                sb.append(para);
                sb.append(",");
            }
            return StringUtils.removeEnd(sb.toString(),",");
        }
    }
}
