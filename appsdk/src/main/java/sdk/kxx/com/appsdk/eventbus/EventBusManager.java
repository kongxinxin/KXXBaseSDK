package sdk.kxx.com.appsdk.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * @version : 1.0
 * @Description :  EventBusManager
 * @Create Date  : 19/1/15  15:37
 */
public class EventBusManager {

    /**
     * 绑定 接受者
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 解绑定
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }


    /**
     * 发送消息(事件)
     */
    public static void sendEvent(MessageEvent event) {
        EventBus.getDefault().post(event);
    }

}
