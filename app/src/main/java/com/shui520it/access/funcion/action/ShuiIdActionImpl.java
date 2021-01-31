package com.shui520it.access.funcion.action;

import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc 根据具体的控件ID寻找 该控件对应的 AccessibilityNodeInfo映射体
 */
public class ShuiIdActionImpl extends ShuiBaseAction<String> {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Nullable
    @Override
    public AccessibilityNodeInfo findFirst(AccessibilityNodeInfo root) {
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByViewId(acitonKey);
        if (isEmpty(list)) {
            return null;
        }
        for (int i = 1; i < list.size(); i++) {//其他的均回收
            list.get(i).recycle();
        }
        return list.get(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Nullable
    @Override
    public List<AccessibilityNodeInfo> findAll(AccessibilityNodeInfo root) {
        return root.findAccessibilityNodeInfosByViewId(acitonKey);
    }
}
