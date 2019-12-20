package com.demo.netty.util;

import com.blackTea.util.threadpool.ThreadFactory_UserDefine;

import java.util.concurrent.*;


/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/3/25
 * @time 11:12
 */
public class ThreadPoolUtil {
    public static ThreadFactory threadFactory = new ThreadFactory_UserDefine("PressureTest");
    public static final ExecutorService pool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2,Runtime.getRuntime().availableProcessors()*2,0L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(1024),threadFactory);

    public static final ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(8);
}
