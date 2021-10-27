package net.henanyuanhang.sms.core.exception;

import java.util.List;
import java.util.Map;

/**
 * 短信发送异常
 */
public class SmsSendException extends Exception {

    private List<String> phoneNumbers;

    private List<String> templateIds;

    private List<Map<String, String>> templateParams;

    public SmsSendException(String message, List<String> phoneNumbers, List<String> templateIds, List<Map<String, String>> templateParams) {
        super(message);
        this.phoneNumbers = phoneNumbers;
        this.templateIds = templateIds;
        this.templateParams = templateParams;
    }

    public SmsSendException(String message, Throwable cause, List<String> phoneNumbers, List<String> templateIds, List<Map<String, String>> templateParams) {
        super(message, cause);
        this.phoneNumbers = phoneNumbers;
        this.templateIds = templateIds;
        this.templateParams = templateParams;
    }
}
