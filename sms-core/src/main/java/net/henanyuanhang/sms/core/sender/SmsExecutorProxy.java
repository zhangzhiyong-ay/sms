package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.common.message.CommonCodeMessage;
import net.henanyuanhang.sms.common.utils.Assert;
import net.henanyuanhang.sms.common.utils.CollectionUtils;
import net.henanyuanhang.sms.common.utils.StringUtils;
import net.henanyuanhang.sms.core.filter.SmsFilter;
import net.henanyuanhang.sms.core.filter.SmsFilterChain;
import net.henanyuanhang.sms.core.sender.result.SendResult;
import net.henanyuanhang.sms.core.sender.result.SendResultData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SmsExecutorProxy implements SmsExecutor {

    private SmsExecutor smsExecutor;
    private CodeMessage codeMessage;
    private SmsFilter[] filters;

    public SmsExecutorProxy(SmsExecutor smsExecutor, List<SmsFilter> filters) {
        this.smsExecutor = smsExecutor;
        this.codeMessage = CommonCodeMessage.getInstance();
        this.filters = filters.toArray(new SmsFilter[0]);
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
    public SendResult send(String phoneNumber, String templateKey, Map<String, String> templateParam) {
        Assert.notEmpty(phoneNumber, "phoneNumber is emtpy");
        Assert.notEmpty(templateKey, "templateKey is emtpy");
        return this.smsExecutor.send(phoneNumber, templateKey, templateParam);
    }

    @Override
    public SendResult send(List<String> phoneNumbers, String templateKey, Map<String, String> templateParam) {
        Assert.notEmpty(phoneNumbers, "phoneNumbers is emtpy");
        Assert.notEmpty(templateKey, "templateKey is emtpy");
        phoneNumbers = phoneNumbers.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        Assert.notNull(phoneNumbers, "phoneNumbers is emtpy");
        return this.smsExecutor.send(phoneNumbers, templateKey, templateParam);
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


    class InnerSmsFilterChainProxy implements SmsFilterChain {
        private SmsFilter[] filters;
        private int currentPosition = 0;
        private SmsExecutor smsExecutor;

        public InnerSmsFilterChainProxy(SmsFilter[] filters, SmsExecutor smsExecutor) {
            this.filters = filters;
        }

        @Override
        public SendResult doFilter(String phoneNumber, String templateKey, Map<String, String> templateParam) {
            SmsFilter nextFilter = this.filters[this.currentPosition];
            SendResult sendResult = nextFilter.doFilter(phoneNumber, templateKey, templateParam, this);
            sendResult.addSendResult(sendResult);
            this.currentPosition++;
            return sendResult;
        }

        @Override
        public boolean hasNext() {
            return this.currentPosition < this.filters.length;
        }
    }
}
