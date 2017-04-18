package com.robusoft.lgd.wtfe.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.robusoft.lgd.wtfe.App;

/**
 * User: lgd(1779964617@qq.com)
 * Date: 2017/4/17
 * Function: 系统服务工具类，主要方便getSystemService方法的调用
 */
public class SystemUtils {
    /**
     * 获取当前进程名
     *
     * @param context context
     * @return 进程名
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取当前连接的网络类型名称
     *
     * @return the name of the network type, such as "WIFI" or "MOBILE".
     */
    public static String getNetworkTypeName() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return "";
        } else {
            return info.getTypeName();
        }
    }

    /**
     * 简单判断当前是否有网络
     */
    public static boolean isNetworkAvailable() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        for (NetworkInfo aNetworkInfo : networkInfo) {
            // 判断当前网络状态是否为连接状态
            if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前连接的网络类型
     *
     * @return one of {@link ConnectivityManager#TYPE_MOBILE},{@link ConnectivityManager#TYPE_WIFI},
     * or other types defined by {@link ConnectivityManager}
     */
    public static int getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return -1;
        } else {
            return info.getType();
        }
    }

    /**
     * 判断当前网络是否是wifi网络
     */
    public static boolean isWifiConnected() {
        return getNetworkType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 隐藏系统键盘
     */
    public static void hideSoftInputFromWindow(@NonNull Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示系统键盘
     */
    public static void showSoftInputFromWindow() {
        ((InputMethodManager) App.getInstance()
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 设置剪切板内容
     */
    public static void setPrimaryClipPlaintText(CharSequence text) {
        ClipboardManager clipboardManager = (ClipboardManager) App.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", text));
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        return App.getInstance().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        return App.getInstance().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕密度
     */
    public static float getDensity() {
        return App.getInstance().getResources().getDisplayMetrics().density;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        Resources resources = App.getInstance().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return -1;
    }
}
