package net.henanyuanhang.sms.core.exception;

import java.util.List;
import java.util.Map;

/**
 * 短信发送异常
 */
public class SmsSendException extends Exception {

    /**
     * 接受短信手机号
     */
    private List<String> phoneNumbers;

    /**
     * 短信发送模板标识
     */
    private List<String> templateKeys;

    /**
     * 短信发送模板参数
     */
    private List<Map<String, String>> templateParams;

    public SmsSendException(String message, List<String> phoneNumbers, List<String> templateKeys, List<Map<String, String>> templateParams) {
        super(message);
        this.phoneNumbers = phoneNumbers;
        this.templateKeys = templateKeys;
        this.templateParams = templateParams;
    }

    public SmsSendException(String message, Throwable cause, List<String> phoneNumbers, List<String> templateKeys, List<Map<String, String>> templateParams) {
        super(message, cause);
        this.phoneNumbers = phoneNumbers;
        this.templateKeys = templateKeys;
        this.templateParams = templateParams;
    }
}
