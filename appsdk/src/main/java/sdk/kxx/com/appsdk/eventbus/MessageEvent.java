package sdk.kxx.com.appsdk.eventbus;

/**
 * @version : 1.0
 * @Description :  MessageEvent 事件对象封装
 * @Create Date  : 19/1/15  15:57
 */
public class MessageEvent<T> {
    /**
     * 标识，防止重复订阅接收
     */
    private int code;
    /**
     * 真正需要的订阅数据
     */
    private T data;

    public MessageEvent(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
