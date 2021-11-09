package net.henanyuanhang.sms.common.message;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class CommonCodeMessage implements CodeMessage {

    private Map<String, String> codeMessageMap;

    private static CommonCodeMessage CODE_MESSAGE = new CommonCodeMessage();

    public static CommonCodeMessage getInstance() {
        return CODE_MESSAGE;
    }

    private CommonCodeMessage() {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        builder.put("COMMON_TEMPLATE_ID_ILLEGAL", "短信模板不合法");
        builder.put("COMMON_PHONE_NUMBER_ILLEGAL", "手机号不合法");
        builder.put("COMMON_SERVICE_ERROR", "短信服务异常");
        builder.put("COMMON_CLIENT_RESPONSE_PARSE_ERROR", "客户端请求结果解析异常");

        codeMessageMap = builder.build();
    }

    public void putCodeMessage(Map<String, String> codeMessageMap) {
        Map<String, String> tmpMap = new HashMap<>(this.codeMessageMap.size() + codeMessageMap.size());
        tmpMap.putAll(this.codeMessageMap);
        tmpMap.putAll(codeMessageMap);

        this.codeMessageMap = ImmutableMap.copyOf(tmpMap);
    }

    public String getMessage(String code) {
        return codeMessageMap.get(code);
    }

    @Override
    public String getMessage(String code, String defaultMessage) {
        String message = getMessage(code);
        return message == null ? defaultMessage : message;
    }


}
