package com.demo.netty.server;

import com.demo.netty.entity.MockDevice;
import com.demo.netty.handler.MockDeviceCodec;
import com.demo.netty.handler.MockDeviceHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MockClient {
    private MockDevice device;
    private String ip;
    private int port;
    private int workers;
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;
    private Channel channel;


    public MockClient(MockDevice device, String ip, int port) {
        device.setTag206Info(StringUtils.join(ip,",",Integer.toHexString(port)));
        this.device = device;
        this.ip = ip;
        this.port = port;
        this.workers = Runtime.getRuntime().availableProcessors()*2;
        this.eventLoopGroup = new NioEventLoopGroup(workers);
        this.bootstrap = new Bootstrap();
    }


    public void connect() {
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0,10000,0, TimeUnit.MILLISECONDS));
                        pipeline.addLast(new MockDeviceCodec());
                        pipeline.addLast(new MockDeviceHandler(device));
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect(ip,port).sync();
            this.channel = channelFuture.channel();
        } catch (InterruptedException e) {
            log.error(String.format("连接Server(IP[%s],PORT[%s])失败", ip, port), e);
        }
    }

    public void stop(){
        try {
            channel.close();
            eventLoopGroup.shutdownGracefully();
            eventLoopGroup = null;
            bootstrap = null;
        } catch (Exception e) {
            log.error("关掉连接发生异常：{}", e);
        }
    }
}
