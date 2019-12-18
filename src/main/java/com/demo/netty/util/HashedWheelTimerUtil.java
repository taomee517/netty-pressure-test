package com.demo.netty.util;

import io.netty.util.HashedWheelTimer;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class HashedWheelTimerUtil {
    public static final int DELAY_TIME = 1000;

    private HashedWheelTimer timer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS);
    private static class SingletonHolder {
        public final static HashedWheelTimerUtil instance = new HashedWheelTimerUtil();
    }

    public static HashedWheelTimerUtil instance() {
        return HashedWheelTimerUtil.SingletonHolder.instance;
    }

    private HashedWheelTimerUtil(){

    }

}
