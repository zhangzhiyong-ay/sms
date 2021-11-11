package net.henanyuanhang.sms.core.sender.result;

import java.util.Map;

/**
 * 短信发送状态
 */
public class SendResultData {

    private String id;

    private String code;

    private String phoneNumber;

    private String templateId;

    private Map<String, String> templateParam;

    private String message;

    private boolean success;

    public static SendResultData create() {
        return new SendResultData();
    }

    public String getId() {
        return id;
    }

    public SendResultData setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public SendResultData setCode(String code) {
        this.code = code;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public SendResultData setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String gettemplateId() {
        return templateId;
    }

    public SendResultData settemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public Map<String, String> getTemplateParam() {
        return templateParam;
    }

    public SendResultData setTemplateParam(Map<String, String> templateParam) {
        this.templateParam = templateParam;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SendResultData setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public SendResultData setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
