package com.demo.netty;

import com.demo.netty.entity.Ex223240Device;
import com.demo.netty.entity.MockDevice;
import com.demo.netty.server.MockClient;
import com.demo.netty.util.FileInfoCheckUtil;
import com.demo.netty.util.HashedWheelTimerUtil;
import com.demo.netty.util.ThreadPoolUtil;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.demo.netty.util.HashedWheelTimerUtil.DELAY_TIME;

@Slf4j
public class PressureTest {
    //    private static String ip = "127.0.0.1";
    private static String ip = "192.168.2.61";
    private static int port = 2103;

    public static void main(String[] args) throws Exception {
        //标明记录压测IMEI号的xls地址
        String filePath = "E:\\private\\test\\pressure test\\压测设备.xlsx";
        List<String> imeis = FileInfoCheckUtil.getColumnData(filePath);

//        List<String> imeis = Arrays.asList("865328026651330","864244028008257");
        int delaySign = 16;
        ExecutorService executor = ThreadPoolUtil.pool;
        for (int i = 0; i < imeis.size(); i++) {
            if(i%delaySign==0){
                Thread.sleep(2000);
            }
            String imei = imeis.get(i);
            Runnable task = new Runnable() {
                @Override
                public void run(){
                    MockDevice device = new Ex223240Device();
                    device.setAgFinish(false);
                    device.setImei(imei);

                    MockClient client = new MockClient(device, ip, port);
                    client.connect();
                }
            };
            executor.submit(task);
        }
    }
}


//        ExecutorService executor = ThreadPoolUtil.pool;
//        for (String imei: imeis) {
//            executor.submit(new Runnable() {
//                @Override
//                public void run() {
//                    String ip = "127.0.0.1";
////                    String ip = "192.168.2.61";
//                    int port = 2103;
//
//                    log.info("线程池 pool = {}", executor);
//
////                    MockDevice device = new MockDevice();
////                    MockDevice device = new Extend240Device();
////                    MockDevice device = new Extend223Device();
//                    MockDevice device = new Ex223240Device();
//                    device.setAgFinish(false);
//                    device.setImei(imei);
//
//                    MockClient client = new MockClient(device,ip,port);
//                    client.connect();
//                }
//            });
//        }
//    }
//}
