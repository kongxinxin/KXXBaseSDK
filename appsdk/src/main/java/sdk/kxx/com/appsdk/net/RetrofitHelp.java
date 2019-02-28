package sdk.kxx.com.appsdk.net;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sdk.kxx.com.appsdk.app.AppConfig;

/**
 * @author : kongxx
 * @Created Date : 2019/2/27 11:49
 * @Description : BaseSDKDemo
 */
public class RetrofitHelp {
    private RetrofitHelp mRetrofitHelp;

    private Retrofit mRetrofit;
    private String BASE_URL = AppConfig.BASE_URL;

    public RetrofitHelp getRetrofitHelp() {
        if (mRetrofitHelp == null) {
            synchronized (RetrofitHelp.this) {
                if (mRetrofitHelp == null) {
                    mRetrofitHelp = new RetrofitHelp();
                }
            }
        }
        return mRetrofitHelp;
    }

    private RetrofitHelp() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (AppConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ParametersInterceptor())
                .addInterceptor(loggingInterceptor)
                .readTimeout(AppConfig.TIMEOUT_SECOND, TimeUnit.SECONDS)
                .connectTimeout(AppConfig.TIMEOUT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(AppConfig.TIMEOUT_SECOND, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }


    public <T> T getService(Class<T> service) {
        if (null == mRetrofit) {
            mRetrofitHelp = null;
            mRetrofitHelp = new RetrofitHelp();
        }
        return mRetrofit.create(service);
    }
}
