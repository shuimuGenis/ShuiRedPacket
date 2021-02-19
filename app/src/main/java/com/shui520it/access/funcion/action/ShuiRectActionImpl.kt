package com.shui520it.access.funcion.action

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo

/**
 * @author shuimu{lwp}
 * @time  2021/2/1 14:10
 * @desc
 */
class ShuiRectActionImpl : ShuiBaseAction<Rect>() {
    private var compareRect: Rect? = null
    override fun checkOk(info: AccessibilityNodeInfo): Boolean {
        val result = compareRect?.let {
            info.getBoundsInScreen(it)
            actionKey?.contains(it)
        }
        return result ?: false
    }
}