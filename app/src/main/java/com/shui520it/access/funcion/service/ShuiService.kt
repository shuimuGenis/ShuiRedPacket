package com.shui520it.access.funcion.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.shui520it.access.funcion.pool.ShuiRunnableImpl
import com.shui520it.access.funcion.pool.ShuiThreadPool

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
class ShuiService : AccessibilityService() {
    override fun onCreate() {
        super.onCreate()
        ShuiThreadPool.runnable
        println(">>>>>>>>>>服务被启动了 onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println(">>>>>>>>>>服务被启动了 onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        when (event?.eventType) {
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                //加个判断避免重复传教Runnable。
                if (ShuiThreadPool.runnable) {
                    ShuiThreadPool.execute(ShuiRunnableImpl(this))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println(">>>>>>>>>>服务被销毁了..")
        ShuiThreadPool.shutdown()
    }

}