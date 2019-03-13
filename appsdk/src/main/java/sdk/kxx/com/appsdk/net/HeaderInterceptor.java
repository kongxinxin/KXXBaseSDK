package sdk.kxx.com.appsdk.net;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import sdk.kxx.com.appsdk.app.AppConfig;

/**
 * @author : kongxx
 * @Created Date : 2019/3/13 10:50
 * @Description : MoleculeLoan_Android
 */
public class HeaderInterceptor implements Interceptor {
    private Map<String, String> headers;

    public HeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        String deviceInfo = android.os.Build.MODEL + ","
                + android.os.Build.VERSION.SDK_INT + ","
                + android.os.Build.VERSION.RELEASE;
        headers.put("User-Agent", deviceInfo);
        headers.put("content-type", "application/json");

        if (null == AppConfig.getHttpListener()) {
            throw new NullPointerException(" IRequestHelperListener 必须初始化");
        }
        AppConfig.getHttpListener().prepareHeader(headers);
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.header(headerKey, headers.get(headerKey)).build();
            }
        }
        return chain.proceed(builder.build());
    }
}
