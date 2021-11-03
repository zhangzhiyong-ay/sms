package net.henanyuanhang.sms.core.sender.result;

/**
 * 短信发送状态
 */
public class SendResultData {

    private String id;

    private String code;

    private String message;

    private boolean success;

    public SendResultData(String id, String code, String message, boolean success) {
        this.id = id;
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
