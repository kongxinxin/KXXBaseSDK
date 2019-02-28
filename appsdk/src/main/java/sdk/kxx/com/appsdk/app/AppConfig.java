package sdk.kxx.com.appsdk.app;

import android.content.Context;

/**
 * @author : kongxx
 * @Created Date : 2019/2/27 14:08
 * @Description : BaseSDKDemo
 */
public class AppConfig {
    private static final String TAG = "AppConfig";
    /**
     * 系统对应的AppKey
     */
    private static String APP_KEY = "";

    public static Context mContext;

    public static String BASE_URL = "";

    public static boolean DEBUG = false;
    /**
     * 设置网络请求读写超时时间 单位：秒
     */
    public static final int TIMEOUT_SECOND = 30;

    public static void initConfig(Context context) {
        if (null == mContext) {
            mContext = context;
        }
    }
}
