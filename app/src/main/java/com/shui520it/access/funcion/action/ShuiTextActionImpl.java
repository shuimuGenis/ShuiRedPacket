package com.shui520it.access.funcion.action;

import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
public class ShuiTextActionImpl extends ShuiBaseAction<String> {
    @Nullable
    @Override
    public AccessibilityNodeInfo findFirst(AccessibilityNodeInfo root) {
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText(acitonKey);
        if (isEmpty(list)) {
            return null;
        }
        if (isEquals) {
            AccessibilityNodeInfo returnInfo = null;
            for (AccessibilityNodeInfo info : list) {
                if (returnInfo == null && info.getText() != null && acitonKey.equals(info.getText().toString())) {
                    returnInfo = info;
                } else {
                    info.recycle();
                }
            }
            return returnInfo;
        } else {
            return list.get(0);
        }
    }

    @Nullable
    @Override
    public List<AccessibilityNodeInfo> findAll(AccessibilityNodeInfo root) {
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText(acitonKey);
        if (isEmpty(list)) {
            return null;
        }
        if (isEquals) {
            ArrayList<AccessibilityNodeInfo> listNew = new ArrayList<>();
            for (AccessibilityNodeInfo info : list) {
                if (info.getText() != null && acitonKey.equals(info.getText().toString())) {
                    listNew.add(info);
                } else {
                    info.recycle();
                }
            }
            return listNew;
        } else {
            return list;
        }
    }
}
