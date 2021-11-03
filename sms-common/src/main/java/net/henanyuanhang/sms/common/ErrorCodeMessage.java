package net.henanyuanhang.sms.common;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ErrorCodeMessage {

    private Map<String, String> ERROR_CODE_MESSAGE;

    private static ErrorCodeMessage errorCodeMessage = new ErrorCodeMessage();


    public static ErrorCodeMessage getInstance() {
        return errorCodeMessage;
    }

    private ErrorCodeMessage() {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        builder.put("COMMON_TEMPLATE_ID_ILLEGAL", "短信模板不合法");
        builder.put("COMMON_PHONE_NUMBER_ILLEGAL", "手机号不合法");
        builder.put("COMMON_SERVICE_ERROR", "短信服务异常");
        builder.put("COMMON_CLIENT_RESPONSE_PARSE_ERROR", "客户端请求结果解析异常");

        ERROR_CODE_MESSAGE = builder.build();
    }

    public synchronized void putErrorCodeMessage(Map<String, String> messages) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        if (ERROR_CODE_MESSAGE != null) {
            builder.putAll(ERROR_CODE_MESSAGE);
        }
        builder.putAll(messages);
        this.ERROR_CODE_MESSAGE = builder.build();
    }


    public String getMessage(String errorCode) {
        return ERROR_CODE_MESSAGE.get(errorCode);
    }


}
