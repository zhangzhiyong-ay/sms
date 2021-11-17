package net.henanyuanhang.sms.htip.example.interceptor;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.core.interceptor.ParamInterceptor;
import net.henanyuanhang.sms.core.sender.result.SendResultData;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 自定义手机号正则表达式拦截器
 *
 * @author zhangzhiyong
 * @createTime 2021年11月17日 22:01
 */
public class PhoneNumberRegexInterceptor implements ParamInterceptor {

    public static String ERROR_CODE = "custom_phoneRegexError";

    private String regex = "^156\\d{8}$";
    private CodeMessage codeMessage;

    public PhoneNumberRegexInterceptor(CodeMessage codeMessage) {
        this.codeMessage = codeMessage;
    }

    @Override
    public SendResultData intercept(String phoneNumber, String templateId, Map<String, String> templateParam) {
        if (Pattern.matches(regex, phoneNumber)) {
            return null;
        }
        return SendResultData.create()
                .setPhoneNumber(phoneNumber)
                .settemplateId(templateId)
                .setTemplateParam(templateParam)
                .setCode(ERROR_CODE)
                .setMessage(codeMessage.getMessage(ERROR_CODE));
    }

}
