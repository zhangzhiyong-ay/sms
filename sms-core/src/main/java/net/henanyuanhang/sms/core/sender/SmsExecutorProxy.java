package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.common.message.CommonCodeMessage;
import net.henanyuanhang.sms.common.utils.Assert;
import net.henanyuanhang.sms.common.utils.CollectionUtils;
import net.henanyuanhang.sms.common.utils.StringUtils;
import net.henanyuanhang.sms.core.sender.result.SendResult;
import net.henanyuanhang.sms.core.sender.result.SendResultData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SmsExecutorProxy implements SmsExecutor {

    private SmsExecutor smsExecutor;
    private CodeMessage codeMessage;

    public SmsExecutorProxy(SmsExecutor smsExecutor) {
        this.smsExecutor = smsExecutor;
        this.codeMessage = CommonCodeMessage.getInstance();
    }

    private SendResultData errorData(String phoneNumber, String templateKey, Map<String, String> templateParams, String code) {
        return SendResultData.create()
                .setSuccess(false)
                .setPhoneNumber(phoneNumber)
                .setTemplateKey(templateKey)
                .setTemplateParam(templateParams)
                .setCode(code)
                .setMessage(codeMessage.getMessage(code));
    }

    @Override
    public SendResult send(String phoneNumber, String templateKey, Map<String, String> templateParams) {
        Assert.notEmpty(phoneNumber, "phoneNumber is emtpy");
        Assert.notEmpty(templateKey, "templateKey is emtpy");
        return this.smsExecutor.send(phoneNumber, templateKey, templateParams);
    }

    @Override
    public SendResult send(List<String> phoneNumbers, String templateKey, Map<String, String> templateParams) {
        Assert.notEmpty(phoneNumbers, "phoneNumbers is emtpy");
        Assert.notEmpty(templateKey, "templateKey is emtpy");
        phoneNumbers = phoneNumbers.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        Assert.notNull(phoneNumbers, "phoneNumbers is emtpy");
        return this.smsExecutor.send(phoneNumbers, templateKey, templateParams);
    }

    @Override
    public SendResult send(List<String> phoneNumbers, List<String> templateKeys, List<Map<String, String>> templateParams) {
        Assert.notEmpty(phoneNumbers, "phoneNumbers is emtpy");
        Assert.notEmpty(templateKeys, "templateKeys is emtpy");
        if (phoneNumbers.size() != templateKeys.size()) {
            throw new IllegalArgumentException("phoneNumbers size not equals tempkateKeys size");
        }
        if (CollectionUtils.notEmpty(templateParams) && templateParams.size() != phoneNumbers.size()) {
            throw new IllegalArgumentException("templateParams size not equals tempkateKeys size");
        }


        return null;
    }
}
