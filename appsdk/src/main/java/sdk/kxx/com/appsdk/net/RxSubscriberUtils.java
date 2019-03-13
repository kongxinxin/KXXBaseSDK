package sdk.kxx.com.appsdk.net;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sdk.kxx.com.appsdk.utils.LogUtils;

/**
 * @author : kongxx
 * @Created Date : 2019/3/1 09:44
 * @Description : RX线程切换
 */
public class RxSubscriberUtils {
    public static final String TAG = "RxSubscriberUtils";

    public static <T> ObservableTransformer<T, T> compose() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                LogUtils.e(TAG,"accept eeeee");
//                                if (!NetworkUtils.checkConnection(WenWoConfig.mContext)) {
//                                    WenWoLog.e(TAG,"网络连接无效");
//                                }else {
//                                    WenWoLog.e(TAG,"网络连接正常");
//                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
