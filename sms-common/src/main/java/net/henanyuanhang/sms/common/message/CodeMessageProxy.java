package net.henanyuanhang.sms.common.message;

import java.util.Map;

public class CodeMessageProxy implements CodeMessage {

    private CommonCodeMessage codeMessage;

    private Map<String, String> customMessage;

    public CodeMessageProxy(Map<String, String> customMessage) {
        this.customMessage = customMessage;
        this.codeMessage = CommonCodeMessage.getInstance();
    }

    public String getMessage(String code) {
        String message = customMessage.get(code);
        if (null == message) {
            message = codeMessage.getMessage(code);
        }
        return message;
    }

    public String getMessage(String code, String defaultMessage) {
        String message = getMessage(code);

        return message == null ? defaultMessage : message;
    }

}
