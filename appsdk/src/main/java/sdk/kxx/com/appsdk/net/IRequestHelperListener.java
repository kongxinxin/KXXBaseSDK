package sdk.kxx.com.appsdk.net;

import java.util.Map;

import sdk.kxx.com.appsdk.model.ErrorItem;

/**
 * @author : kongxx
 * @Created Date : 2019/3/8 17:31
 * @Description : 监听请求的整个过程
 */
public interface IRequestHelperListener {
    /**
     * 用于追加公用接口
     */
    public void prepareRequest(Map<String, String> map);

    /**
     * 用于追加header
     */
    public void prepareHeader(Map<String, String> headers);

    /**
     * 打印请求的日志
     */
    public boolean isLog();

    /**
     * 个别需要处理的错误，或者统一的错误处理
     */
    public void onParseResponseEnd(ErrorItem commonResult);
}
