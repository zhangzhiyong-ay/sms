package net.henanyuanhang.sms.htip.sender;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.common.utils.JSONUtils;
import net.henanyuanhang.sms.core.sender.SmsExecutor;
import net.henanyuanhang.sms.core.sender.result.SendResult;
import net.henanyuanhang.sms.core.sender.result.SendResultData;
import net.henanyuanhang.sms.htip.HtipProperties;
import net.henanyuanhang.sms.htip.client.SmsClient;
import net.henanyuanhang.sms.htip.client.SmsSendResponse;
import net.henanyuanhang.sms.htip.exception.HtipClientException;
import net.henanyuanhang.sms.htip.message.HtipCodeMessageHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 中国电信综合短信业务管理平台短信
 */
public class HtipSmsExecutor implements SmsExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtipSmsExecutor.class);

    private SmsClient smsClient;

    private CodeMessage codeMessage;

    public HtipSmsExecutor(HtipProperties htipProperties) {
        this.smsClient = new SmsClient(htipProperties);
        this.codeMessage = HtipCodeMessageHolder.getInstance();
    }

    public CodeMessage getCodeMessage() {
        return codeMessage;
    }

    public void setCodeMessage(CodeMessage codeMessage) {
        this.codeMessage = codeMessage;
    }

    public SmsClient getSmsClient() {
        return smsClient;
    }

    public void setSmsClient(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    /**
     * 执行发送
     *
     * @param phoneNumber   手机号
     * @param templateId    短信模板key
     * @param templateId    短信模板ID。如果为null时，则尝试根据tempkateKey获取
     * @param templateParam 模板参数
     * @return
     */
    private SendResultData doSend(String phoneNumber, String templateId, Map<String, String> templateParam) {

        try {
            SmsSendResponse smsSendResponse = smsClient.sendSms(phoneNumber, templateId, templateParam);
            SendResultData success = SendResultData.create()
                    .setSuccess(true)
                    .setId(smsSendResponse.getMessageId())
                    .setCode(smsSendResponse.getCode())
                    .setMessage(smsSendResponse.getDescription())
                    .setPhoneNumber(phoneNumber)
                    .settemplateId(templateId)
                    .setTemplateParam(templateParam);
            return success;
        } catch (HtipClientException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("短信发送异常", e);
            }
            SendResultData fail = SendResultData.create()
                    .settemplateId(templateId)
                    .setTemplateParam(templateParam)
                    .setPhoneNumber(phoneNumber)
                    .setCode(e.getCode())
                    .setSuccess(false)
                    .setMessage(e.getMessage());
            return fail;
        }
    }

    @Override
    public SendResult send(String phoneNumber, String templateId, Map<String, String> templateParam) {
        SendResultData sendResultData = this.doSend(phoneNumber, templateId, templateParam);
        SendResult sendResult = SendResult.create().addSendResultData(sendResultData);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("短信发送结果：{}", JSONUtils.toString(sendResult));
        }
        return sendResult;
    }

    @Override
    public SendResult send(List<String> phoneNumbers, String templateId, Map<String, String> templateParam) {
        SendResult sendResult = SendResult.create();

        for (String phoneNumber : phoneNumbers) {
            SendResultData sendResultData = this.doSend(phoneNumber, templateId, templateParam);
            sendResult.addSendResultData(sendResultData);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("短信发送结果：{}", JSONUtils.toString(sendResult));
        }
        return sendResult;
    }

    @Override
    public SendResult send(List<String> phoneNumbers, List<String> templateIds, List<Map<String, String>> templateParams) {
        Iterator<String> phoneNumberIterator = phoneNumbers.iterator();
        Iterator<String> templateIdIterator = templateIds.iterator();
        boolean noneTemplateParam = null == templateParams;
        Iterator<Map<String, String>> templateParamIterator = noneTemplateParam ? null : templateParams.iterator();

        String phoneNumber, templateId;
        Map<String, String> templateParam;

        SendResult sendResult = SendResult.create();
        while (phoneNumberIterator.hasNext()) {
            phoneNumber = phoneNumberIterator.next();
            templateId = templateIdIterator.next();
            templateParam = noneTemplateParam ? null : templateParamIterator.next();

            SendResultData sendResultData = this.doSend(phoneNumber, templateId, templateParam);
            sendResult.addSendResultData(sendResultData);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("短信发送结果：{}", JSONUtils.toString(sendResult));
        }
        return sendResult;
    }

}
