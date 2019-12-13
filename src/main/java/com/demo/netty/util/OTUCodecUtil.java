/**
 *
 */
package com.demo.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class OTUCodecUtil {
    private static final byte leftWrap = '(';
    private static final byte rightWrap = ')';


    /**
     * 是否是登录消息
     *
     * @param msg
     * @return
     */
//    public static boolean isAddressMsg(Message msg) {
//        if (isHeartMsg(msg)) {//心跳消息
//            return false;
//        }
//        String tag = new String(msg.getFunction());
//        return tag.equalsIgnoreCase("a1");
//    }

    /**
     * 是否是登录消息
     *
     * @param msg
     * @return
     */
//    public static boolean isLoginMsg(Message msg) {
//        if (isHeartMsg(msg)) {//心跳消息
//            return false;
//        }
//        String tag = new String(msg.getFunction());
//        return tag.equalsIgnoreCase("a3");
//    }

    /**
     * 是否是心跳消息
     *
     * @param msg
     * @return
     */
//    public static boolean isHeartMsg(Message msg) {
//        return null == msg || null == msg.getFunction();
//    }


    /**
     * 将文本转化为nettyMsg
     *
     * @param in
     * @return
     * @throws Exception
     */
//    public static MsgSerializedResult deSerialize(String in) throws Exception {
//        try {
//            if (StringUtils.isBlank(in)) {
//                return null;
//            }
//            String strData;
//            //是否是加密消息:以*开头，并且消息头不为|a1|
//            boolean isEncryptMsg = in.startsWith("*") && !"|a1|".equalsIgnoreCase(in.substring(3,7));
//            //寻址消息没有加密, 其它消息都是加密的 (*b0|a1|1
//            if (!isEncryptMsg && StringUtils.substring(StringUtils.split(in, "*")[1],3, 5).equalsIgnoreCase("a1") ) {
//                strData = in;
//            } else {
//                if (isEncryptMsg) {
//                    strData = CRCUtil.asiccCrcDecode(in);
//                } else {
//                    strData = CRCUtil.isEncoded(in);
//                }
//            }
//            if (StringUtils.isBlank(strData)) {
//                return null;
//            }
//            String[] reqArr = strData.split("\\|");
//            if (isEncryptMsg) {
//                if (!validateCRC(strData, reqArr)) {
//                    throw new Exception("校验失败:" + strData);
//                }
//            }
//
//            byte[] function = reqArr[1].getBytes();
//            Message result = new Message();
//            result.setFunction(function);
//
//            for (int i = 2; i < reqArr.length; ++i) {
//                String[] content = reqArr[i].split(",");
//                String tag = content[0];
//                if (tag.equals(")")) {
//                    break;
//                }
//                String value = "";
//                if (reqArr[i].indexOf(',') != -1) {
//                    value = reqArr[i].substring(reqArr[i].indexOf(',') + 1);
//                }
//                result.getTvList().add(new TV(tag.getBytes(), value.getBytes()));
//            }
//            return new MsgSerializedResult(strData, result);
//        } catch (Exception e) {
//            log.error("解析错误消息：msg = {}", in);
//            throw e;
//        }
//    }

    private static boolean validateCRC(String in, String[] reqArr) {
        String crc = reqArr[0].substring(1);
        String body = in.split("\\" + reqArr[0] + "\\|")[1];
        byte[] b = body.getBytes();
        byte crcByte = 0;
        for (int j = 0; j < b.length; ++j) {
            crcByte += b[j];
        }
        if (!CRCUtil.getCRCByteHexValue(crcByte).equals(crc)) {
            return false;
        }
        return true;
    }

    /**
     * 生成原生的响应消息
     *
     * @param outMsg
     * @return
     */
//    private static String generateResponseStr(Message outMsg) {
//        StringBuilder sb = new StringBuilder(new String(outMsg.getFunction()));
//        sb.append("|");
//        for (TV tv : outMsg.getTvList()) {
//            sb.append(tv.getStrTag()).append(",").append(tv.getStrValue()).append("|");
//        }
//        return sb.toString();
//    }

    /**
     * 序列化，把对象转成字符串
     */
//    public static String serialize(Message outMsg) throws Exception {
//        try {
//            //			if (null == outMsg || outMsg.getTvList().isEmpty()) {//心跳消息
//            //				logger.error("无法发送空的消息,{}", outMsg);
//            //				return null;
//            //			}
//            if (OTUCodecUtil.isHeartMsg(outMsg)) {//是心跳消息.
//                return "";
//            }
//            String reponseStr = generateResponseStr(outMsg);
//            String result = StringUtils.joint(CRCUtil.getCRCByteStr(reponseStr), reponseStr);
//            return result;
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    /**
     * bytebuf 装维ascii码  字符串
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String Byte2StringSerialize(ByteBuf in) throws Exception {
//        int length = in.readableBytes();
//        if (length < 2) {
//            return null;
//        }
//        int readStart = in.readerIndex();
//        int startIndex = -1;
//        int endIndex = -1;
//        byte currentByte = 0;
//        for (int index = 0; index < length; index++) {
//            currentByte = in.readByte();
//            if (leftWrap == currentByte) {
//                startIndex = index;
//            } else if (startIndex >= 0 && rightWrap == currentByte) {
//                endIndex = index;
//                int contentLength = endIndex - startIndex - 1;
//                in.readerIndex(readStart + startIndex + 1);
//                byte[] result = new byte[contentLength];
//                in.readBytes(result, 0, contentLength);
//                in.readByte();
//                return new String(result, "UTF-8");
//            }
//        }
//        if (startIndex >= 0) {
//            in.readerIndex(readStart + startIndex);
//        }
//        return null;

        //找到第一个报文起始符的下标
        int startSignIndex = in.forEachByte(new ByteProcessor.IndexOfProcessor(leftWrap));
        if(startSignIndex==-1){
            return null;
        }
        //将readerIndex置为起始符下标
        in.readerIndex(startSignIndex);

        //找到第一个报文结束符的下标
        int endSignIndex = in.forEachByte(new ByteProcessor.IndexOfProcessor(rightWrap));
        if(endSignIndex == -1 || endSignIndex < startSignIndex){
            return null;
        }
        //计算报文的总长度
        //此处不能去操作writerIndex,否则只能截取到第一条完整报文
        int length = endSignIndex - startSignIndex + 1;
        //将报文内容写入符串，并返回
        byte[] data = new byte[length];
        in.readBytes(data);
        String temp = new String(data);
        String result = temp.substring(temp.indexOf(leftWrap) + 1,temp.indexOf(rightWrap));
        return result;
    }

}
