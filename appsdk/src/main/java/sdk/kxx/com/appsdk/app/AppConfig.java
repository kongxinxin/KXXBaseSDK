package sdk.kxx.com.appsdk.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import sdk.kxx.com.appsdk.net.IRequestHelperListener;
import sdk.kxx.com.appsdk.utils.LogUtils;
import sdk.kxx.com.appsdk.utils.PackageUtils;


/**
 * @author : kongxx
 * @Created Date : 2019/2/27 14:08
 * @Description : APP全局定义
 */
public class AppConfig {

    private static final String TAG = "AppConfig";
    /**
     * 系统对应的AppKey
     */
    private static String APP_KEY = "";

    public static Context mContext;

    public static String BASE_URL = "";

    public static boolean DEBUG = true;
    /**
     * 设置网络请求读写超时时间 单位：秒
     */
    public static final int TIMEOUT_SECOND = 30;

    private static IRequestHelperListener httpListener = null;

    public static void initConfig(Context context) {
        if (null == mContext) {
            mContext = context;
        }
    }

    /**
     * 配置系统常量
     */
    public interface SystemConstants {

        String REQUEST_TIME = "times";
        String REQUEST_SIGN = "sign";
        String SESSION_ID = "token";
        String USER_ID = "userId";
        /**
         * 存储文件sd卡根路径
         */
        String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/molecule";
        String DBPATH = ROOT + "/db";
        String LOG = ROOT + "/log";

        /**
         * 临时文件路径
         */
        String TEMP = ROOT + "/temp/";

        /**
         * 缓存路径
         */
        String CACHE = ROOT + "/cache/";

        /**
         * 图片缓存路径
         */
        String IMAGE_CACHE = ROOT + "/image/";

        /**
         * 拍照或者相册选择图片路径
         */
        String IMAGE_CAMERA = ROOT + "/camera/";

        /**
         * 下载目录(文件及图片下载管理)
         */
        String DOWNLOAD = ROOT + "/download/";

        /**
         * 升级目录(升级文件管理)
         */
        String UPGRADE = ROOT + "/upgrade/";
    }

    public static int getPacakgeVersion() {
        int code = PackageUtils.getVersionCode(mContext);
        return code;
    }


    public static IRequestHelperListener getHttpListener() {
        return httpListener;
    }

    public static void setHttpListener(IRequestHelperListener httpListener) {
        AppConfig.httpListener = httpListener;
    }


    public static String getAppKey() {
        return APP_KEY;
    }

    public static void setAppKey(String appKey) {
        APP_KEY = appKey;
    }

    /**
     * 设置SP值
     *
     * @param key
     * @param value
     */
    public static void setSPValue(String key, Object value) {
        setValue(key, value);
    }

    /**
     * 删除SP值
     *
     * @param key
     */
    public static void removeSPValue(String key) {
        setValue(key, null);
    }


    /**
     * 获取string类型数据
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return
     */
    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);

        return sp.getString(key, defaultValue);
    }

    /**
     * 获取string类型数据
     *
     * @param key key值
     * @return
     */
    public static String getString(String key) {

        return getString(key, "");
    }


    /**
     * 获取float类型数据
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return
     */
    public static float getFloat(String key, float defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);

        return sp.getFloat(key, defaultValue);
    }

    /**
     * 获取float类型数据
     *
     * @param key key值
     * @return
     */
    public static float getFloat(String key) {

        return getFloat(key, 0.0f);
    }

    /**
     * 获取boolean类型数据
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);

        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 获取boolean类型数据
     *
     * @param key key值
     * @return
     */
    public static boolean getBoolean(String key) {

        return getBoolean(key, false);
    }

    /**
     * 获取int类型数据
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return
     */
    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);

        return sp.getInt(key, defaultValue);
    }

    /**
     * 获取int类型数据
     *
     * @param key key值
     * @return
     */
    public static int getInt(String key) {

        return getInt(key, 0);
    }

    /**
     * 获取long类型数据
     *
     * @param key          key值
     * @param defaultValue 默认值
     * @return
     */
    public static long getLong(String key, long defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);

        return sp.getLong(key, defaultValue);
    }

    /**
     * 获取long类型数据
     *
     * @param key key值
     * @return
     */
    public static long getLong(String key) {

        return getLong(key, 0l);
    }

    /**
     * 设置参数
     *
     * @param key
     * @param value
     */
    private static void setValue(String key, Object value) {
        try {

            if (null == mContext) {
                throw new NullPointerException("mContext  不能为空");
            }
            //保持到数据
//            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DOCTOR_CONFIG,Context.MODE_PRIVATE);
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            if (null == value) {
                edit.remove(key);
            } else {
                if (value instanceof String) {
                    edit.putString(key, value.toString());
                } else if (value instanceof Integer) {
                    edit.putInt(key, (Integer) value);
                } else if (value instanceof Long) {
                    edit.putLong(key, (Long) value);
                } else if (value instanceof Float) {
                    edit.putFloat(key, (Float) value);
                } else if (value instanceof Boolean) {
                    edit.putBoolean(key, (Boolean) value);
                }
            }
            edit.commit();
        } catch (Exception e) {
            LogUtils.e(TAG, "setValue error:" + e);
        }
    }

    /**
     * 清除所有的SharedPreferences
     */
    public static void cleanAllSPvalue() {


        if (null == mContext) {
            throw new NullPointerException("mContext  不能为空");
        }
//        SharedPreferences sharedPreferences= mContext.getSharedPreferences(DOCTOR_CONFIG,Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();


    }

}

