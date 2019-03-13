package sdk.kxx.com.appsdk.net;

/**
 * @author : kongxx
 * @Description : 网络请求返回基类
 * @Created Date : 2019/3/1 09:38
 */
public class BaseResult<T> {

    private T data;
    private String code;
    private String dataStamp;
    private String msg;
    private String traceId;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataStamp() {
        return dataStamp;
    }

    public void setDataStamp(String dataStamp) {
        this.dataStamp = dataStamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
