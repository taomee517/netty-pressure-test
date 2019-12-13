package com.demo.netty;

import com.demo.netty.entity.MockDevice;
import com.demo.netty.server.MockClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PressureTest {
    public static void main(String[] args) {

        String imei = "156888888888811";
        String ip = "192.168.2.61";
        int port = 2103;

        MockDevice device = new MockDevice();
        device.setAgFinish(false);
        device.setImei(imei);

        MockClient client = new MockClient(device,ip,port);
        client.connect();
    }
}
