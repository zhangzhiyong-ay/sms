package net.henanyuanhang.sms.htip.sender;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.common.utils.JSONUtils;
import net.henanyuanhang.sms.common.utils.StringUtils;
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

    private Map<String, String> templates;

    private CodeMessage codeMessage;

    public HtipSmsExecutor(HtipProperties htipProperties) {
        this.smsClient = new SmsClient(htipProperties);
        this.templates = htipProperties.getTemplate();
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
     * @param phoneNumber    手机号
     * @param templateKey    短信模板key
     * @param templateId     短信模板ID。如果为null时，则尝试根据tempkateKey获取
     * @param templateParam 模板参数
     * @return
     */
    private SendResultData doSend(String phoneNumber, String templateKey, String templateId, Map<String, String> templateParam) {
        if (StringUtils.isEmpty(templateId)) {
            templateId = this.templates.get(templateKey);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("tempkateKey[{}]匹配到的templateId为：[{}]", templateKey, templateId);
            }
        }

        if (StringUtils.isEmpty(templateId)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("templateId 值为空");
            }
            SendResultData fail = SendResultData.create()
                    .setTemplateKey(templateKey)
                    .setTemplateParam(templateParam)
                    .setPhoneNumber(phoneNumber)
                    .setCode("E000005")
                    .setSuccess(false)
                    .setMessage(codeMessage.getMessage("E000005"));
            return fail;
        } else {
            try {
                SmsSendResponse smsSendResponse = smsClient.sendSms(phoneNumber, templateId, templateParam);
                SendResultData success = SendResultData.create()
                        .setSuccess(true)
                        .setId(smsSendResponse.getMessageId())
                        .setCode(smsSendResponse.getCode())
                        .setMessage(smsSendResponse.getDescription())
                        .setPhoneNumber(phoneNumber)
                        .setTemplateKey(templateKey)
                        .setTemplateParam(templateParam);
                return success;
            } catch (HtipClientException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.error("短信发送异常", e);
                }
                SendResultData fail = SendResultData.create()
                        .setTemplateKey(templateKey)
                        .setTemplateParam(templateParam)
                        .setPhoneNumber(phoneNumber)
                        .setCode(e.getCode())
                        .setSuccess(false)
                        .setMessage(e.getMessage());
                return fail;
            }
        }
    }

    @Override
    public SendResult send(String phoneNumber, String templateKey, Map<String, String> templateParam) {
        SendResultData sendResultData = this.doSend(phoneNumber, templateKey, null, templateParam);
        SendResult sendResult = SendResult.builder().addSendData(sendResultData).build();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("短信发送结果：{}", JSONUtils.toString(sendResult));
        }
        return sendResult;
    }

    @Override
    public SendResult send(List<String> phoneNumbers, String templateKey, Map<String, String> templateParam) {
        SendResult.SendResultBuilder builder = SendResult.builder();

        String templateId = this.templates.get(templateKey);

        for (String phoneNumber : phoneNumbers) {
            SendResultData sendResultData = this.doSend(phoneNumber, templateKey, templateId, templateParam);
            builder.addSendData(sendResultData);
        }
        SendResult sendResult = builder.build();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("短信发送结果：{}", JSONUtils.toString(sendResult));
        }
        return sendResult;
    }

    @Override
    public SendResult send(List<String> phoneNumbers, List<String> templateKeys, List<Map<String, String>> templateParams) {
        Iterator<String> phoneNumberIterator = phoneNumbers.iterator();
        Iterator<String> templateKeyIterator = templateKeys.iterator();
        boolean noneTemplateParam = null == templateParams;
        Iterator<Map<String, String>> templateParamIterator = noneTemplateParam ? null : templateParams.iterator();

        String phoneNumber, templateKey;
        Map<String, String> templateParam;

        SendResult.SendResultBuilder builder = SendResult.builder();
        while (phoneNumberIterator.hasNext()) {
            phoneNumber = phoneNumberIterator.next();
            templateKey = templateKeyIterator.next();
            templateParam = noneTemplateParam ? null : templateParamIterator.next();

            SendResultData sendResultData = this.doSend(phoneNumber, templateKey, null, templateParam);
            builder.addSendData(sendResultData);
        }
        SendResult sendResult = builder.build();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("短信发送结果：{}", JSONUtils.toString(sendResult));
        }
        return sendResult;
    }

}
