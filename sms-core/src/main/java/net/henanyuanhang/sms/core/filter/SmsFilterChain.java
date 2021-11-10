package net.henanyuanhang.sms.core.filter;

import net.henanyuanhang.sms.core.sender.result.SendResult;

import java.util.Map;

public interface SmsFilterChain {

    /**
     * 发送短信过滤器链
     *
     * @param phoneNumber   手机号
     * @param templateKey   短信模板key
     * @param templateParam 短信模板参数
     * @return
     */
    SendResult doFilter(String phoneNumber, String templateKey, Map<String, String> templateParam);

    boolean hasNext();
//
//    /**
//     * 同一模板批量发送短信过滤器链
//     *
//     * @param phoneNumbers  手机号集合
//     * @param templateKey   短信模板key
//     * @param templateParam 短信模板参数
//     * @return
//     */
//    SendResult doFilter(List<String> phoneNumbers, String templateKey, Map<String, String> templateParam);
//
//    /**
//     * 不同模板批量发送短信过滤器链
//     *
//     * @param phoneNumbers   手机号集合
//     * @param templateKeys   短信模板key集合
//     * @param templateParams 短信模板参数集合
//     * @return
//     */
//    SendResult doFilter(List<String> phoneNumbers, List<String> templateKeys, List<Map<String, String>> templateParams);
}
