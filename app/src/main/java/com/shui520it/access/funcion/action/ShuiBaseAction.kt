package com.shui520it.access.funcion.action

import android.view.accessibility.AccessibilityNodeInfo

/**
 * @author shuimu{lwp}
 * @time  2021/2/1 13:44
 * @desc
 */
abstract class ShuiBaseAction<T> {
    /**
     * 包含 or 相等
     */
    open var isEquals = true

    /**
     * 处理的数据
     */
   open var actionKey: T? = null

    /**
     * 检查是否符合要求，默认true
     */
    open fun checkOk(info: AccessibilityNodeInfo) = true

    /**
     * 返回集合的第一个
     */
    open fun findFirst(info: AccessibilityNodeInfo): AccessibilityNodeInfo? = null

    /**
     * 返回集合
     */
    open fun findAll(info: AccessibilityNodeInfo): List<AccessibilityNodeInfo>? = null
}