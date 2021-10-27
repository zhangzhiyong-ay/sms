package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.core.exception.SmsSendException;
import net.henanyuanhang.sms.core.sender.result.SendResult;

import java.util.List;
import java.util.Map;

/**
 * 短信发送者
 */
public interface SmsSender {

    /**
     * 单个发送短信
     *
     * @param phoneNumber    发送短信的手机号，不可为空
     * @param templateId     短信模板标识，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    SendResult send(String phoneNumber, String templateId, Map<String, String> templateParams) throws SmsSendException;

    /**
     * 批量发送-同一短信内容向不同手机号发送
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateId     短信模板标识，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    SendResult send(List<String> phoneNumbers, String templateId, Map<String, String> templateParams) throws SmsSendException;

    /**
     * 批量发送-向不同手机号发送不同短信内容
     * 要求手机号、短信模板标识、短信模板参数字段个数相同，一一对应。
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateIds    短信模板标识，不可为空，且长度要与 phoneNumbers 字段个数一致
     * @param templateParams 短信模板参数。如果所有模板都无参数时，可以将该参数设置为null，
     *                       否则需要与 phoneNumbers 字段个数相同。如其中一个模板无参数时，可对该下标的值设置为null。
     * @return
     */
    SendResult send(List<String> phoneNumbers, List<String> templateIds, List<Map<String, String>> templateParams) throws SmsSendException;
}
