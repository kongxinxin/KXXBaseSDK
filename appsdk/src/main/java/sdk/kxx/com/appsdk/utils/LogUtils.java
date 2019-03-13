package sdk.kxx.com.appsdk.utils;

import android.util.Log;

import sdk.kxx.com.appsdk.app.AppConfig;

/**
 * @author : kongxx
 * @Description : 日志工具类
 * @Created Date : 2019/3/1 09:40
 */
public class LogUtils {

    protected static final String TAG = "LogUtils";

    public static void d(String msg) {

        if (AppConfig.DEBUG) {
            Log.d(TAG, msg);

        }
    }

    public static void e(String msg) {
        if (AppConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (AppConfig.DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (AppConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }
}
