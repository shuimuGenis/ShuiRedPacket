package com.shui520it.access.funcion.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
public class ShuiThreadPool {
    private static ShuiThreadPool INSTANCE;
    private ExecutorService executorService;
    public boolean mRunnable = true;

    private ShuiThreadPool() {
        init();
    }

    public static ShuiThreadPool get() {
        if (INSTANCE == null) {
            synchronized (ShuiThreadPool.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ShuiThreadPool();
                }
            }
        }
        return INSTANCE;
    }

    public void init() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
    }

    public synchronized void execute(Runnable run) {
        if (mRunnable) {
            mRunnable = false;
            executorService.execute(run);
        }
    }

    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
        executorService = null;
    }

}
