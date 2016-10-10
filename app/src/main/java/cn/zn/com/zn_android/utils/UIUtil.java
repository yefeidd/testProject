package cn.zn.com.zn_android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cn.zn.com.zn_android.manage.RnApplication;

/**
 * Handler工具类
 * <p>
 * Created by Jolly on 2016/3/16 0016.
 */
public class UIUtil {

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return RnApplication.getMainThreadHandler();
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 从主线程looper里面移除runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    /**
     * 获取主线程
     *
     * @return
     */
    public static Thread getMainThread() {
        return RnApplication.getMainThread();
    }

    /**
     * 获取主线程ID
     *
     * @return
     */
    public static long getMainThreadId() {
        return RnApplication.getMainThreadId();
    }

    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取全局的上下文
     */
    public static Context getContext() {
        return RnApplication.getInstance();
    }

    /**
     * 隐藏软键盘
     */
    public static void hidekeyBoard(View view) {
        //1.得到InputMethodManager对象
        InputMethodManager imm = (InputMethodManager) RnApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        //2.调用hideSoftInputFromWindow方法隐藏软键盘
        imm.hideSoftInputFromWindow(view.getWindowToken(),0); //强制隐藏键盘
    }

    /**
     * 判断是否是快速点击该按钮
     */
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
