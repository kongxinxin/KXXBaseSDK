package sdk.kxx.com.appsdk.model;

/**
 * @author : kongxx
 * @Created Date : 2019/2/27 11:31
 * @Description : BaseSDKDemo
 */
public class ErrorItem {
    private String errorCode;
    private String errorMessage;

    public ErrorItem(String errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorItem(String code, String errors) {
        this.errorCode = code;
        this.errorMessage = errors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
