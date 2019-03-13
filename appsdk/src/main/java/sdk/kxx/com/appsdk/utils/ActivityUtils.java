package sdk.kxx.com.appsdk.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.List;

/**
 * @author : kongxx
 * @Created Date : 2019/3/2 17:55
 * @Description : 界面工具类
 */
public class ActivityUtils {
    //显示虚拟键盘
    public static void ShowKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );

        imm.showSoftInput(v,InputMethodManager.SHOW_FORCED);

    }
    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        BeanUtils.checkNotNull(fragmentManager);
        BeanUtils.checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * 判断应用是否是前台运行
     *
     * @param context
     * @return
     */
    public static boolean isForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 安装APK
     * @param context 上下文
     * @param filePath 文件路径
     */
    public static void installApk(Context context, String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static int getWindowWidth(Activity activity) {
        // 分辨率
        DisplayMetrics dm = new DisplayMetrics();
        // 获取分辨率
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm.widthPixels;
    }

    public static int getWindowHeight(Activity activity) {
        // 分辨率
        DisplayMetrics dm = new DisplayMetrics();
        // 获取分辨率
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;
    }

    public static Point getDisplayPixelSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * 屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayPixelWidth(Context context) {
        Point size = getDisplayPixelSize(context);
        return (size.x);
    }

    /**
     * 屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDisplayPixelHeight(Context context) {
        Point size = getDisplayPixelSize(context);
        return (size.y);
    }

    public static float spToPx(Context context, float sp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final float scale = displayMetrics.scaledDensity;
        return sp * scale;
    }

    public static int dpToPx(Context context, int dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
        return (int) px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5);
    }

    /**
     * 获取屏幕信息
     */
    public static void loadScreenInfo(Activity context) {
        // 分辨率
        DisplayMetrics dm = new DisplayMetrics();
        // 获取分辨率
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        LogUtils.e("屏幕密度:" + dm.densityDpi + " 屏幕分辨率:" + dm.widthPixels + "*" + dm.heightPixels);
    }

    //测量宽高
    public static void DisplayViewWidthandHeight(final View view) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                LogUtils.e("width=" + view.getWidth() + " --- height=" + view.getHeight() + "---top=" + view.getTop());
            }
        });
    }


    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }


}
