package net.henanyuanhang.sms.core.interceptor;

import net.henanyuanhang.sms.core.sender.result.SendResultData;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 手机号正则表达式拦截器
 */
public abstract class PhoneNumberRegexParamInterceptor implements ParamInterceptor {

    private String regex;

    public PhoneNumberRegexParamInterceptor(String regex) {
        this.regex = regex;
    }

    @Override
    public SendResultData intercept(String phoneNumber, String templateId, Map<String, String> templateParam) {
        boolean matches = Pattern.matches(regex, phoneNumber);
        return matches ? matchSuccess(phoneNumber, templateId, templateParam) : matchFail(phoneNumber, templateId, templateParam);
    }

    /**
     * 正则表达式匹配成功
     *
     * @param phoneNumber
     * @param templateId
     * @param templateParam
     * @return
     */
    protected abstract SendResultData matchSuccess(String phoneNumber, String templateId, Map<String, String> templateParam);

    /**
     * 正则表达式匹配失败
     *
     * @param phoneNumber
     * @param templateId
     * @param templateParam
     * @return
     */
    protected abstract SendResultData matchFail(String phoneNumber, String templateId, Map<String, String> templateParam);
}
