package sdk.kxx.com.appsdk.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import sdk.kxx.com.appsdk.app.AppConfig;
import sdk.kxx.com.appsdk.utils.BeanUtils;
import sdk.kxx.com.appsdk.utils.LogUtils;
import sdk.kxx.com.appsdk.utils.StringUtils;

/**
 * @author : kongxx
 * @Created Date : 2019/2/27 14:01
 * @Description : BaseSDKDemo
 */
public class ParametersInterceptor implements Interceptor {
    public static final String TAG = "ParametersInterceptor";
    public static final String STR_JSON = "json";
    public static final String STR_FORM = "form";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        Map<String, String> requestParam = new HashMap<>();
        //处理添加的公共参数
        AppConfig.getHttpListener().prepareRequest(requestParam);

        HttpUrl url = originalHttpUrl.newBuilder().build();
        Iterator iterator = requestParam.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            url = url.newBuilder()
                    .addQueryParameter(entry.getKey(), entry.getValue()).build();
        }


        TreeMap<String, String> treeMap = new TreeMap<>();
        RequestBody requestBody = original.body();
        if (null != requestBody && null != requestBody.contentType()) {

            String subtype = requestBody.contentType().subtype();
            if (subtype.contains(STR_JSON)) {
                processJson(treeMap, requestBody);
            } else if (subtype.contains(STR_FORM)) {
                processFormData(treeMap, requestBody);
            }
        }

        if (StringUtils.isNotEmpty(url.encodedQuery())) {
            String[] querys = url.encodedQuery().split("&");
            for (int i = 0; i < querys.length; i++) {
                String parameter = querys[i];
                int position = parameter.indexOf("=");
                treeMap.put(parameter.substring(0, position), parameter.substring(position + 1, parameter.length()));
            }
        }


        Request.Builder requestBuilder = original.newBuilder()
                .url(encrypt(url, treeMap));

        LogUtils.d(TAG, "╔═══════════════════════════" + requestBuilder.build().method() + "请求参数═══════════════════════════════════");

        LogUtils.d(TAG, "║请求URL:" + url);
        String[] querysAll = requestBuilder.build().url().encodedQuery().split("&");
        for (int i = 0; i < querysAll.length; i++) {
            String parameter = querysAll[i];
            int position = parameter.indexOf("=");
            LogUtils.d(TAG, "║  " + "[" + parameter.substring(0, position) + ":" + parameter.substring(position + 1, parameter.length()) + "]");
        }
        LogUtils.d(TAG, "║ 完整URL：" + requestBuilder.build().url().toString());
        LogUtils.d(TAG, "╚═══════════════════════════════════════════════════════════════════════════");

        Request request = requestBuilder.build();

        return chain.proceed(request);
    }

    /**
     * 解析-body json 数据
     * {需要确认服务器接收@ResponseBody ,否则服务器无法获取完整的参数，导致请求校验无法通过}
     */
    private void processJson(TreeMap<String, String> treeMap, RequestBody requestBody) {

        LogUtils.d(TAG, "processJson----------->");

        try {
            JSONObject jsonObject = new JSONObject(bodyToString(requestBody));
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next();
                String value = jsonObject.getString(key);
                treeMap.put(key, value);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析-form表单数据
     */
    private void processFormData(TreeMap<String, String> treeMap, RequestBody requestBody) {
        LogUtils.d(TAG, "processFormData----------->");
        String bodyString = bodyToString(requestBody);
        if (StringUtils.isNotEmpty(bodyString)) {

            LogUtils.e(TAG, "processFormData body:" + bodyString);

            String[] body = bodyString.split("&");
            for (int i = 0; i < body.length; i++) {
                String parameter = body[i];
                int position = parameter.indexOf("=");
                treeMap.put(parameter.substring(0, position), parameter.substring(position + 1, parameter.length()));
            }

        } else {
            LogUtils.e(TAG, "body null");
        }
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    //追加 sign、times
    public HttpUrl encrypt(HttpUrl url, TreeMap<String, String> treeMap) {

        String datetime = BeanUtils.formatDate(new Date(), "yyyyMMddHHmmss");


//        treeMap.put(AppConfig.SystemConstants.REQUEST_TIME, datetime);
//        StringBuffer message = new StringBuffer();
//
//        for (Map.Entry<String, String> temp : treeMap.entrySet()) {
//            message.append(temp.getKey());
//            if (temp.getValue() != null) {
//                message.append(temp.getValue());
//            }
//        }
//        LogUtils.e("加密前:" + AppConfig.getAppKey() + message.toString());
//        String urlDecoder = "";
//        try {
//            urlDecoder = URLDecoder.decode(message.toString(), "UTF-8");
//            LogUtils.e("URLDecoder 加密前:" + AppConfig.getAppKey() + urlDecoder);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String sign = BeanUtils.md532(AppConfig.getAppKey() + urlDecoder);
        return url.newBuilder().addQueryParameter(AppConfig.SystemConstants.REQUEST_TIME, datetime).build();
    }
}
