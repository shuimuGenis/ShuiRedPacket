package com.shui520it.access.funcion.pool;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import androidx.annotation.RequiresApi;

import com.shui520it.access.funcion.action.ShuiBaseAction;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
public class ShuiRunnableImpl implements Runnable {
    /**
     * 红包所在容器
     */
    public static final String CHAT_OPEN_REDView_ID = "com.tencent.mm:id/auf";
    /**
     * 红包“开”按钮的ViewID
     */
    public static final String CHAT_OPEN_REDKEY_ID = "com.tencent.mm:id/f4f";
    /**
     * "已领取"的控件ID
     */
    public static final String CHAT_OPEN_RECEIVED_ID = "com.tencent.mm:id/tt";
    /**
     * 每个微信红包都有一个叫"微信红包"的 小文字 显示,我们根据这个来判断确实是微信红包
     */
    public static final String CHAT_WX_HONGBAO_FLAG_ID = "com.tencent.mm:id/u5";
    /**
     * 微信主页的ID
     */
    public static final String CHAT_MAIN_ID = "com.tencent.mm:id/g8f";
    /**
     * 判断是否进入了 查看红包金额的界面了
     * 条件一CHAT_REDKEY_RESULT_ID 条件二CHAT_REDKEY_RESULT_TEXT
     */
    public static final String CHAT_REDKEY_RESULT_ID = "com.tencent.mm:id/ezz";
    public static final String CHAT_REDKEY_RESULT_TEXT_01 = "已存入零钱，可";
    public static final String CHAT_WX_HONGBAO_FLA_TEXT = "微信红包";

    public WeakReference<AccessibilityService> serviceReference;

    public ShuiRunnableImpl(AccessibilityService s) {
        serviceReference = new WeakReference<>(s);
    }

    @Override
    public void run() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            boolean mainPager = isMainPager();
            if (mainPager) {
                ShuiThreadPool.get().mRunnable = true;
                return;
            }
            try {
                Thread.sleep(250);
                AccessibilityService service = serviceReference.get();
                if (service == null) {
                    return;
                }
                List windows = service.getWindows();
                for (Object window : windows) {
                    AccessibilityNodeInfo root = ((AccessibilityWindowInfo) window).getRoot();
                    boolean alreadyInRedKeyResultPager = isAlreadyInRedKeyResultPager(root);
                    if (alreadyInRedKeyResultPager) {
                        ShuiThreadPool.get().mRunnable = true;
                        return;
                    }
                    if (root != null && !isAlreadyInRedKeyDetailPager(root)) {
                        //因为红包会存在多个，那么哪怕是通过ID获取的也会有多个
                        List redketRootList = root.findAccessibilityNodeInfosByViewId(CHAT_OPEN_REDView_ID);
                        if (!ShuiBaseAction.isEmpty(redketRootList)) {
                            for (int i = 0; i < redketRootList.size(); i++) {
                                AccessibilityNodeInfo A = (AccessibilityNodeInfo) redketRootList.get(i);//得到第一层
                                if (A.getChildCount() > 1) {
                                    AccessibilityNodeInfo child = A.getChild(1);
                                    if (child == null) {
                                        A.recycle();
                                        continue;
                                    }
                                    //判断是否有"微信红包"这种文字表示,有就是微信红包
                                    List<AccessibilityNodeInfo> flagList = A.findAccessibilityNodeInfosByViewId(CHAT_WX_HONGBAO_FLAG_ID);
                                    if (flagList == null || flagList.isEmpty()) {
                                        System.out.println(">>>>>>>>>>不是微信红包");
                                        A.recycle();
                                        continue;
                                    }
                                    CharSequence text = flagList.get(0).getText();
                                    boolean b = CHAT_WX_HONGBAO_FLA_TEXT.contentEquals(text);
                                    if (!b) {
                                        System.out.println(">>>>>>>>>>不是微信红包");
                                        continue;
                                    }
                                    //0代表里面没有已领取的字 说明红包没有被领取
                                    if (child.findAccessibilityNodeInfosByViewId(CHAT_OPEN_RECEIVED_ID).size() == 0) {
                                        System.out.println(">>>>>>>>>>没有领取..那就点击这个红包");
                                        A.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        child.recycle();
                                    } else {
                                        System.out.println(">>>>>>>>>>已被领取>>继续查找未领取的红包");
                                    }
                                }
                                A.recycle();
                            }
                        }
                        root.recycle();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ShuiThreadPool.get().mRunnable = true;
    }


    /**
     * 判断当前是否已经是在 红包的详情页面了，是的话 直接点击"开"红包的按钮
     *
     * @param root
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean isAlreadyInRedKeyDetailPager(AccessibilityNodeInfo root) {
        //查找红包"开"这个按钮
        List openRedKetButtonList = root.findAccessibilityNodeInfosByViewId(CHAT_OPEN_REDKEY_ID);
        if (openRedKetButtonList != null && openRedKetButtonList.size() > 0) {
            System.out.println(">>>>当前是 红包的详情页面了，直接点击“开”红包按钮 ");
            AccessibilityNodeInfo openRedKetButtonInfo = (AccessibilityNodeInfo) openRedKetButtonList.get(0);
            openRedKetButtonInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            openRedKetButtonList.clear();
            isAlreadyInRedKeyResultPager(root);
            return true;
        }
        return false;
    }

    /**
     * 判断是否是 打开红包之后的 查看红包结果的页面了
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean isAlreadyInRedKeyResultPager(AccessibilityNodeInfo root) {
        if (root == null) {
            return false;
        }
        List<AccessibilityNodeInfo> textList = root.findAccessibilityNodeInfosByViewId(CHAT_REDKEY_RESULT_ID);
        if (textList == null) {
            return false;
        }
        CharSequence text;
        for (AccessibilityNodeInfo info : textList) {
            text = info.getText();
            if (text == null || text.length() < 1) {
                continue;
            }
            if (text.toString().startsWith(CHAT_REDKEY_RESULT_TEXT_01)) {
                System.out.println(">>>>>>>>>>现在是 查看红包金额结果 页面");
                AccessibilityService service = serviceReference.get();
                if (service == null) {
                    return false;
                }
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                textList.clear();
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是不是微信主页，主页就不需要执行操作了
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean isMainPager() {
        AccessibilityService service = serviceReference.get();
        if (service == null) {
            return false;
        }
        AccessibilityNodeInfo rootInActiveWindow = service.getRootInActiveWindow();
        if (rootInActiveWindow == null) {
            return false;
        }
        List<AccessibilityNodeInfo> chatMainIdList = rootInActiveWindow.findAccessibilityNodeInfosByViewId(ShuiRunnableImpl.CHAT_MAIN_ID);
        if (chatMainIdList != null && chatMainIdList.size() > 0) {
            System.out.println(">>>>>>>>>>现在是微信主页");
            return true;
        }
        return false;
    }
}
