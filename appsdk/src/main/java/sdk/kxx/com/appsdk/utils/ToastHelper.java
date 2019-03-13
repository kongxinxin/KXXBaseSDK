package sdk.kxx.com.appsdk.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * @author : kongxx
 * @Created Date : 2019/3/2 17:54
 * @Description : Toast弹出工具
 */
public class ToastHelper {

    private static final String TAG = "ToastHelper";

    /**
     * 默认ID
     */
    public static final int TOAST_ID_DEFAULT = 0;

    /**
     * 传输管理ID
     */
    public static final int TOAST_ID_TRRANSFER = 1;

    /**
     * context
     */
    private static Context sAppContext;

    private static SparseArray<Toast> mToast = new SparseArray<Toast>();

    private static Handler sHandler;

    private static final int FREQUENCY = 2000;

    private static String msgMsg;

    private static long lastShowTime;

    /**
     * 初始化Context
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (!isMainThread()) {
            throw new RuntimeException("must call in main thread!");
        }
        sAppContext = context;
        sHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int toastId = msg.arg1;
                String textMsg = (String) msg.obj;
                boolean global = msg.arg2 == 1;
                showToast(toastId, textMsg, global);
            }
        };
    }

    private static boolean isMainThread() {
        Looper currLooper = Looper.myLooper();
        if (currLooper == null || currLooper != Looper.getMainLooper()) {
            return false;
        }
        return true;
    }

    private static void showToast(int toastId, String msg, boolean global) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        //添加一个逻辑，不是前台不提醒
        if (!global && !ActivityUtils.isForeground(sAppContext)) {
            LogUtils.e(TAG, "showToast & app is background");
            return;
        }
        //两秒内，连续相同的msg不显示
        Long time = System.currentTimeMillis() - lastShowTime;
        if (time < FREQUENCY && msg != null && msg.equals(msgMsg)) {
            return;
        }

        if (!isMainThread()) {
            Message msg1 = sHandler.obtainMessage();
            msg1.arg1 = toastId;
            msg1.arg2 = 0;
            if (global) {
                msg1.arg2 = 1;
            }
            msg1.obj = msg;
            msg1.sendToTarget();
            return;
        }

        msgMsg = msg;
        lastShowTime = System.currentTimeMillis();

        int duration = (msg.length() > 50) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        if (toastId > 0) {
            Toast toast = mToast.get(toastId);

            if (toast == null) {
                synchronized (ToastHelper.class) {

                    toast = Toast.makeText(sAppContext, msg, duration);
                    mToast.put(toastId, toast);
                }
            } else {

                toast.setText(msg);
                toast.setDuration(duration);

            }
            toast.show();
        } else {
            Toast.makeText(sAppContext, msg, duration).show();
        }

    }

    /***
     * 展示toast
     * @param context context
     * @param toastId toastId
     * @param msgResId msgResId
     */
    public static void showToast(Context context, int toastId, int msgResId) {
        showToast(toastId, context.getString(msgResId), false);
    }

    /***
     * 展示toast
     * @param context context
     * @param msgResId msgResId
     */
    public static void showDefaultToast(Context context, int msgResId) {
        showToast(TOAST_ID_DEFAULT, context.getString(msgResId), false);
    }

    /***
     * 展示toast
     * @param msg msg
     */
    public static void showDefaultToast(String msg) {
        showToast(TOAST_ID_DEFAULT, msg, false);
    }

    /**
     * 传输管理的Toast
     *
     * @param context  context
     * @param msgResId msgResId
     */
    public static void showTransferToast(Context context, int msgResId) {
        showToast(TOAST_ID_TRRANSFER, context.getString(msgResId), false);
    }

    /**
     * 程序在后台也显示toast
     *
     * @param context context
     * @param msg     msg
     */
    public static void showGlobalToast(Context context, String msg) {
        showToast(TOAST_ID_DEFAULT, msg, true);
    }

    public static int dpToPx(Context context, int dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
        return (int) px;
    }

    /**
     * 成功 样式
     */
    public static final int TOAST_SUCCESS = 1;
    /**
     * 失败样式
     */
    public static final int TOAST_FAILED = 2;
}
