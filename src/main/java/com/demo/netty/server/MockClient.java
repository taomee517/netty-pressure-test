package com.demo.netty.server;

import com.demo.netty.entity.MockDevice;
import com.demo.netty.handler.MockDeviceCodec;
import com.demo.netty.handler.MockDeviceHandler;
import com.demo.netty.util.HashedWheelTimerUtil;
import com.demo.netty.util.ThreadPoolUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.util.concurrent.TimeUnit;

import static com.demo.netty.util.HashedWheelTimerUtil.DELAY_TIME;

@Slf4j
@Data
public class MockClient {
    private MockDevice device;
    private String ip;
    private int port;
    private int workers;
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;
    private Channel channel;


    public MockClient(MockDevice device, String ip, int port) {
        if (!Boolean.TRUE.equals(device.isAgFinish())) {
            device.setTag206Info(StringUtils.join(ip,",",Integer.toHexString(port)));
            device.setTag20fInfo(StringUtils.join(ip,",",Integer.toHexString(port)));
        }
        device.setTag101Info(device.getImei());
        this.device = device;
        this.ip = ip;
        this.port = port;
        this.workers = Runtime.getRuntime().availableProcessors()*2;
        this.eventLoopGroup = new NioEventLoopGroup(workers);
    }


    public Bootstrap buildBootstrap(){
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0,1000*60*2,0, TimeUnit.MILLISECONDS));
                        pipeline.addLast(new MockDeviceCodec());
                        pipeline.addLast(new MockDeviceHandler(MockClient.this,device));
                    }
                });
        return bootstrap;
    }

    public void connect() {
        this.bootstrap = buildBootstrap();
        try {
            ChannelFuture channelFuture = bootstrap.connect(ip,port).sync();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            ThreadPoolUtil.pool.submit(new Runnable() {
                @Override
                public void run() {
                    log.info("连接失败，重连");
                    connect();
                }
            });
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
