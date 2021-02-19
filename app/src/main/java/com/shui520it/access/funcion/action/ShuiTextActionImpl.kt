package com.shui520it.access.funcion.action

import android.view.accessibility.AccessibilityNodeInfo

/**
 * @author shuimu{lwp}
 * @time  2021/2/1 14:17
 * @desc
 */
class ShuiTextActionImpl : ShuiBaseAction<String>() {
    override fun findFirst(info: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        val findResultsByText = info.findAccessibilityNodeInfosByText(actionKey)
        return if (isEquals) {
            var b: Boolean
            findResultsByText?.find {
                b = it.text != null && actionKey.equals(it.text.toString())
                if (!b) it.recycle()
                b
            }
        } else
            findResultsByText?.get(0)
    }

    override fun findAll(info: AccessibilityNodeInfo): List<AccessibilityNodeInfo>? {
        val findResultByText = info.findAccessibilityNodeInfosByText(actionKey)
        return if (isEquals) {
            var b: Boolean
            findResultByText.filter {
                b = it.text != null && actionKey.equals(it.text.toString())
                if (!b) it.recycle()
                b
            }
        } else
            findResultByText
    }
}