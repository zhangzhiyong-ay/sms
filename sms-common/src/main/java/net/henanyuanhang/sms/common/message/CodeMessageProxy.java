package net.henanyuanhang.sms.common.message;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class CodeMessageProxy implements CodeMessage {

    private CommonCodeMessage commonCodeMessage;

    private Map<String, String> customMessage;

    public CodeMessageProxy(Map<String, String> customMessage) {
        this.customMessage = ImmutableMap.copyOf(customMessage);
        this.commonCodeMessage = CommonCodeMessage.getInstance();
    }

    public String getMessage(String code) {
        String message = customMessage.get(code);
        if (null == message) {
            message = commonCodeMessage.getMessage(code);
        }
        return message;
    }

    public String getMessage(String code, String defaultMessage) {
        String message = getMessage(code);

        return message == null ? defaultMessage : message;
    }

}
