package com.demo.netty;

import com.demo.netty.entity.Ex223240Device;
import com.demo.netty.entity.MockDevice;
import com.demo.netty.entity.RequestType;
import com.demo.netty.server.MockClient;
import com.demo.netty.util.FileInfoCheckUtil;
import com.demo.netty.util.MessageBuilder;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class PressureTest {
//    private static String ip = "pre.acceptor.mysirui.com";
//    private static String ip = "192.168.6.232";
//    private static String ip = "127.0.0.1";
    private static String ip = "192.168.2.61";
    private static int port = 2103;

    public static void main(String[] args) throws Exception {
        //标明记录压测IMEI号的xls地址
//        String filePath = "E:\\private\\test\\pressure test\\yxd.xlsx";
        String filePath = "E:\\private\\test\\pressure test\\压测设备.xlsx";
        List<String> imeis = FileInfoCheckUtil.getColumnData(filePath);

//        List<String> imeis = Arrays.asList("232999900801682");
//        List<String> imeis = Arrays.asList("156888888888833","156888888888811");
        int delaySign = 16;
        int size = imeis.size();
        for (int i=0; i<size; i++) {
//            if(i%delaySign==0){
//                Thread.sleep(5000);
//            }
            Thread.sleep(500);
            String imei = imeis.get(i);
            MockDevice device = new Ex223240Device();
            device.setAgFinish(false);
            device.setImei(imei);
            MockClient client = new MockClient(device, ip, port);
            Channel channel = client.connect();
            String msg = MessageBuilder.buildAgAsMsg(RequestType.AG,device);
            channel.writeAndFlush(msg);
        }
    }
}
