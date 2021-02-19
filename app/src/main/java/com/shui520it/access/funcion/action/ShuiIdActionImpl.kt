package com.shui520it.access.funcion.action

import android.os.Build
import android.view.accessibility.AccessibilityNodeInfo

/**
 * @author shuimu{lwp}
 * @time  2021/2/1 13:58
 * @desc 根据具体的控件ID寻找 该控件对应的 AccessibilityNodeInfo映射体
 */
class ShuiIdActionImpl : ShuiBaseAction<String>() {

    override fun findFirst(info: AccessibilityNodeInfo): AccessibilityNodeInfo? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) info.findAccessibilityNodeInfosByViewId(actionKey)?.takeIf { it.isNotEmpty() }?.first() else null

    override fun findAll(info: AccessibilityNodeInfo): List<AccessibilityNodeInfo>? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) info.findAccessibilityNodeInfosByViewId(actionKey) else null

}