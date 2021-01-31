package com.shui520it.access.funcion.action;

import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Collection;
import java.util.List;

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
public abstract class ShuiBaseAction<T> {
    /**
     * 是包含还必须相等;
     */
    public boolean isEquals = true;
    public T acitonKey;

    /**
     * 子类检查是否 就是 正寻找的内容对象
     */
    public boolean checkOk(AccessibilityNodeInfo thisInfo) {
        return true;
    }

    /**
     * 返回一个 寻找内容
     *
     * @param root 被寻找的父类
     * @return
     */
    @Nullable
    public AccessibilityNodeInfo findFirst(AccessibilityNodeInfo root) {
        return null;
    }

    /**
     * 返回一组寻找内容
     *
     * @param root 被寻找的父类
     * @return
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Nullable
    public List<AccessibilityNodeInfo> findAll(AccessibilityNodeInfo root) {
        return null;
    }

    /**
     * 集合是否是空的
     */
    public static boolean isEmpty(Collection list) {
        return list == null || list.size() == 0;
    }
}
