package sdk.kxx.com.appsdk.net;

import android.support.annotation.NonNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import sdk.kxx.com.appsdk.app.AppConfig;
import sdk.kxx.com.appsdk.model.ErrorItem;
import sdk.kxx.com.appsdk.utils.LogUtils;
import sdk.kxx.com.appsdk.utils.NetworkUtils;
import sdk.kxx.com.appsdk.utils.StringUtils;

/**
 * @author : kongxx
 * @Created Date : 2019/3/1 09:37
 * @Description : 网络请求返回处理类
 */
public abstract class BaseObserver<T> implements Observer<BaseResult<T>> {

    private static final String TAG = "BaseObserver";
    public static final String RET_OK = "ok";

    protected Disposable disposable;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
        LogUtils.e(TAG, "onSubscribe");
    }

    @Override
    public void onNext(@NonNull BaseResult<T> tBaseResult) {
        LogUtils.e(TAG, "onNext");
        if (StringUtils.isNotEmpty(tBaseResult.getCode())) {
            LogUtils.d(TAG, "请求成功 返回 onNext----------->");
            if (String.valueOf(200).equals(tBaseResult.getCode()) || String.valueOf(1000).equals(tBaseResult.getCode())) {
                onSuccess(tBaseResult.getData());
            } else {
                ErrorItem errorItem = new ErrorItem(String.valueOf(tBaseResult.getCode()), tBaseResult.getMsg());
                onFailure(errorItem);
            }
        } else {
            LogUtils.d(TAG, "请求 返回----------->");
            ErrorItem errorItem = new ErrorItem(String.valueOf(tBaseResult.getCode()), tBaseResult.getMsg());
            onFailure(errorItem);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtils.e(TAG, "onError:" + e.getMessage());
        if (!NetworkUtils.checkConnection(AppConfig.mContext)) {
            LogUtils.e(TAG, "网络连接无效");
            ErrorItem errorItem = new ErrorItem("4040", e.getMessage());
            onFailure(errorItem);
            if (null != disposable && !disposable.isDisposed()) {
                disposable.dispose();
            }
        } else {
            LogUtils.e(TAG, "网络连接正常");
            // TODO: 2017/12/22 此时应该上报错误信息
            ErrorItem errorItem = new ErrorItem("404", e.getMessage());
            onFailure(errorItem);
            if (null != disposable && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    @Override
    public void onComplete() {
        LogUtils.e(TAG, "onComplete");
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    /**
     * 请求业务成功
     */
    public abstract void onSuccess(T t);

    /**
     * 请求业务失败
     */
    public abstract void onFailure(ErrorItem errorItem);
}
