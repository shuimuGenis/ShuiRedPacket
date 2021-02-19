package com.shui520it.access.funcion.pool

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
object ShuiThreadPool {
    var runnable = true

    /**
     * 一个线程的线程池
     */
    private val executorService: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }

    /**
     * 加上线程安全
     */
    fun execute(run: Runnable) {
        synchronized(ShuiThreadPool) {
            if (runnable) {
                runnable = false
                executorService.execute(run)
            }
        }
    }

    fun shutdown() {
        executorService.shutdown()
    }
}