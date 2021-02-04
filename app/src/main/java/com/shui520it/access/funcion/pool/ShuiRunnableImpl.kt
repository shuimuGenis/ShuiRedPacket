package com.shui520it.access.funcion.pool

import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi
import java.lang.ref.WeakReference

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
class ShuiRunnableImpl(service: AccessibilityService) : Runnable {
    private val serviceReference: WeakReference<AccessibilityService> = WeakReference(service)

    override fun run() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isMainPager()) {
                ShuiThreadPool.runnable = true
                return
            }
            try {
                Thread.sleep(100)
                serviceReference.get()?.windows?.forEach {
                    if (isAlreadyInRedKeyResultPager(it.root)) {
                        return@forEach
                    }
                    it.root?.takeIf { rootIt -> !isAlreadyInRedKeyDetailPager(rootIt) }?.findAccessibilityNodeInfosByViewId(CHAT_OPEN_REDView_ID)?.forEach constituting@{ ait ->
                        if (ait.childCount > 1) {
                            val child = ait.getChild(1)
                            if (child == null) {
                                ait?.recycle()
                                return@constituting
                            }
                            //判断是否有"微信红包"这种文字表示,有就是微信红包
                            val flagList = ait.findAccessibilityNodeInfosByViewId(CHAT_WX_HONGBAO_FLAG_ID)
                            if (flagList.isNullOrEmpty()) {
                                println(">>>>>>>>>>不是微信红包")
                                ait?.recycle()
                                return@constituting
                            }
                            if (!CHAT_WX_HONGBAO_FLA_TEXT.contentEquals(flagList[0].text)) {
                                println(">>>>>>>>>>不是微信红包")
                                return@constituting
                            }
                            if (child.findAccessibilityNodeInfosByViewId(CHAT_OPEN_RECEIVED_ID).size == 0) {
                                println(">>>>>>>>>>没有领取..那就点击这个红包")
                                ait?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                child.recycle()
                                ait?.recycle()
                                return@forEach
                            } else
                                println(">>>>>>>>>>已被领取>>继续查找未领取的红包")
                        }
                        ait?.recycle()
                    }
                    it.root?.recycle()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        ShuiThreadPool.runnable = true
    }

    /**
     * 判断当前是否 已经显示 红包弹窗了，是的话，点击"开"红包的按钮
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun isAlreadyInRedKeyDetailPager(root: AccessibilityNodeInfo): Boolean {
        root.findAccessibilityNodeInfosByViewId(CHAT_OPEN_REDKEY_ID)?.takeIf { it.size > 0 }?.first()?.let {
            println(">>>>当前是 红包的详情页面了，直接点击“开”红包按钮 ")
            it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            isAlreadyInRedKeyResultPager(root)
            return true
        }
        return false
    }

    /**
     * 判断是否已经是红包结果查看页面了
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun isAlreadyInRedKeyResultPager(root: AccessibilityNodeInfo?): Boolean {
        root?.findAccessibilityNodeInfosByViewId(CHAT_REDKEY_RESULT_ID)?.forEach constituting@{
            if (it.text.isNullOrEmpty()) {
                return@constituting
            }
            if (it.text.toString().startsWith(CHAT_REDKEY_RESULT_TEXT_01)) {
                println(">>>>>>>>>>现在是 查看红包金额结果 页面")
                serviceReference.get()?.apply {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
                    return true
                }
            }
        }
        return false
    }

    /**
     * 判断当前是不是微信主页
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun isMainPager(): Boolean {
        serviceReference.get()?.rootInActiveWindow?.findAccessibilityNodeInfosByViewId(CHAT_MAIN_ID)?.let {
            val notEmpty = it.isNotEmpty()
            if (notEmpty) {
                println(">>>>>>>>>>现在是微信主页")
            }
            return notEmpty
        }
        return false
    }

    companion object {
        /**
         * 红包所在容器
         */
        internal val CHAT_OPEN_REDView_ID = "com.tencent.mm:id/auf"

        /**
         * 红包“开”按钮的ViewID
         */
        internal val CHAT_OPEN_REDKEY_ID = "com.tencent.mm:id/f4f"

        /**
         * "已领取"的控件ID
         */
        internal val CHAT_OPEN_RECEIVED_ID = "com.tencent.mm:id/tt"

        /**
         * 每个微信红包都有一个叫"微信红包"的 小文字 显示,我们根据这个来判断确实是微信红包
         */
        internal val CHAT_WX_HONGBAO_FLAG_ID = "com.tencent.mm:id/u5"

        /**
         * 微信主页的ID
         */
        internal val CHAT_MAIN_ID = "com.tencent.mm:id/g8f"

        /**
         * 判断是否进入了 查看红包金额的界面了
         * 条件一CHAT_REDKEY_RESULT_ID 条件二CHAT_REDKEY_RESULT_TEXT
         */
        internal val CHAT_REDKEY_RESULT_ID = "com.tencent.mm:id/ezz"
        internal val CHAT_REDKEY_RESULT_TEXT_01 = "已存入零钱，可"
        internal val CHAT_WX_HONGBAO_FLA_TEXT = "微信红包"
    }
}