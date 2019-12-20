package com.demo.netty.util;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author taomee
 */
public class ChannelSession {
    public static final AttributeKey<ConcurrentHashMap<String, Object>> SESSION_KEY = AttributeKey.newInstance("context");
    public static final String DEVICE = "device";
    public static final String CLIENT = "client";
    public static final String SAME_SERVER = "sameServer";

    private static synchronized void initQueue(Channel ctx) {
        ConcurrentHashMap<String, Object> map = ctx.attr(SESSION_KEY).get();
        if (null == map) {
            ctx.attr(SESSION_KEY).set(new ConcurrentHashMap());
        }

    }

    public static ConcurrentHashMap<String, Object> getMap(Channel ctx) {
        ConcurrentHashMap<String, Object> map = ctx.attr(SESSION_KEY).get();
        if (null == map) {
            initQueue(ctx);
            map = ctx.attr(SESSION_KEY).get();
        }

        return map;
    }

    public static Object get(Channel ctx, String key) {
        return getMap(ctx).get(key);
    }

    public static void put(Channel ctx, String key, Object obj) {
        getMap(ctx).put(key, obj);
    }

    public static void remove(Channel ctx, String key) {
        getMap(ctx).remove(key);
    }

    public static void removeAll(Channel ctx) {
        ctx.attr(SESSION_KEY).set(null);
    }
}
