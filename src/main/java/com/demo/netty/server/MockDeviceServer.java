package com.demo.netty.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MockDeviceServer {
    private String imei;
    private String ip;
    private int port;
    private int workers;
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;


    public MockDeviceServer(String imei, String ip, int port) {
        this.imei = imei;
        this.ip = ip;
        this.port = port;
        this.workers = Runtime.getRuntime().availableProcessors();
        this.eventLoopGroup = new NioEventLoopGroup(workers);
        this.bootstrap = new Bootstrap();
    }




    public void start(){
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class);
    }
}
