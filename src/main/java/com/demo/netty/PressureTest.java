package com.demo.netty;

import com.demo.netty.entity.MockDevice;
import com.demo.netty.server.MockClient;
import com.demo.netty.util.FileInfoCheckUtil;
import com.demo.netty.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
public class PressureTest {
    public static void main(String[] args) throws Exception{
        //标明记录压测IMEI号的xls地址
        String filePath = "E:\\private\\test\\pressure test\\压测设备.xlsx";
        List<String> teminalImeis = FileInfoCheckUtil.getColumnData(filePath);
        ExecutorService executor = ThreadPoolUtil.pool;
        for (String imei: teminalImeis) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    String ip = "192.168.2.61";
                    int port = 2103;

                    MockDevice device = new MockDevice();
                    device.setAgFinish(false);
                    device.setImei(imei);

                    MockClient client = new MockClient(device,ip,port);
                    client.connect();
                }
            });
        }
    }
}
