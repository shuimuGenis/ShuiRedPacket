package com.shui520it.access.funcion.action;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc 获取指定区域
 */
public class ShuiRectActionImpl extends ShuiBaseAction<Rect> {
    private Rect compareData;
    @Override
    public boolean checkOk(AccessibilityNodeInfo thisInfo) {
        if (compareData != null) {
            thisInfo.getBoundsInScreen(compareData);
            return acitonKey.contains(compareData);
        }
        return false;
    }
}
