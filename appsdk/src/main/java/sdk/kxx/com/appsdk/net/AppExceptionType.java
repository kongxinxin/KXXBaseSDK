package sdk.kxx.com.appsdk.net;

/**
 * @author : kongxx
 * @Created Date : 2019/3/11 09:51
 * @Description : MoleculeLoan_Android
 */
public enum AppExceptionType {
    MAINTAIN("服务器正在维护中，请稍候重试。。。"),
    COMMON_ERROR("服务器正忙，请稍候重试。。。"),
    CAN_NOT_ACCESS("没有权限"),
    REQUEST_INVALIDATE("非法请求"),
    REQUEST_USER_INVALIDATE("您的账号涉嫌违规，如需帮助，请与客服联系。"),
    AID_NULL("aid空"),
    CONTENT_EMPTY("提交内容为空"),
    CONTENT_TOO_LONG("提交内容过长"),
    SESSION_TIMEOUT("登录过期，请重新登录"),
    INVALID_USERID("无效用户id"),
    PARAME_NOT_FULL("参数不完整");
    private String value;

    private AppExceptionType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String toString() {
        return value;
    }
}
