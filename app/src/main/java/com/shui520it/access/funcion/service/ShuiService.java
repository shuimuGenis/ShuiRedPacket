package com.shui520it.access.funcion.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import com.shui520it.access.funcion.pool.ShuiRunnableImpl;
import com.shui520it.access.funcion.pool.ShuiThreadPool;

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
public class ShuiService extends AccessibilityService {

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println(">>>>>>>>>>服务被启动了 onCreate");
        ShuiThreadPool.get().init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println(">>>>>>>>>>服务被启动了 onStartCommand");
        ShuiThreadPool.get().init();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开启无障碍服务后，将会得到监听页面的所有事件。
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED == eventType) {
            ShuiThreadPool.get().execute(new ShuiRunnableImpl(this));
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println(">>>>>>>>>>服务被销毁了..");
        ShuiThreadPool.get().shutdown();
    }
}
