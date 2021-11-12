package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.common.message.CommonCodeMessage;
import net.henanyuanhang.sms.common.utils.Assert;
import net.henanyuanhang.sms.common.utils.CollectionUtils;
import net.henanyuanhang.sms.core.interceptor.ParamInterceptor;
import net.henanyuanhang.sms.core.interceptor.ParamInterceptorManager;
import net.henanyuanhang.sms.core.sender.result.SendResult;
import net.henanyuanhang.sms.core.sender.result.SendResultData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SmsExecutorProxy implements SmsExecutor {

    private SmsExecutor smsExecutor;
    private CodeMessage codeMessage;
    private final List<ParamInterceptor> smsSendInterceptors;

    public SmsExecutorProxy(SmsExecutor smsExecutor, ParamInterceptorManager sendInterceptorManager) {
        this.smsExecutor = smsExecutor;
        this.codeMessage = CommonCodeMessage.getInstance();
        this.smsSendInterceptors = sendInterceptorManager.getInterceptors();
    }

    private SendResultData errorData(String phoneNumber, String templateId, Map<String, String> templateParams, String code) {
        return SendResultData.create()
                .setSuccess(false)
                .setPhoneNumber(phoneNumber)
                .settemplateId(templateId)
                .setTemplateParam(templateParams)
                .setCode(code)
                .setMessage(codeMessage.getMessage(code));
    }

    /**
     * 执行拦截器
     *
     * @param phoneNumber
     * @param templateId
     * @param templateParam
     * @return
     */
    private SendResultData doInterceptor(String phoneNumber, String templateId, Map<String, String> templateParam) {
        SendResultData sendResultData = null;
        for (ParamInterceptor smsSendInterceptor : smsSendInterceptors) {
            sendResultData = smsSendInterceptor.intercept(phoneNumber, templateId, templateParam);
            if (sendResultData != null) {
                break;
            }
        }
        return sendResultData;
    }

    @Override
    public SendResult send(String phoneNumber, String templateId, Map<String, String> templateParam) {
        Assert.notEmpty(phoneNumber, "phoneNumber is emtpy");
        Assert.notEmpty(templateId, "templateId is emtpy");
        SendResultData sendResultData = doInterceptor(phoneNumber, templateId, templateParam);
        if (sendResultData != null) {
            return SendResult.create().addSendResultData(sendResultData);
        }
        return this.smsExecutor.send(phoneNumber, templateId, templateParam);
    }

    @Override
    public SendResult send(List<String> phoneNumbers, String templateId, Map<String, String> templateParam) {
        Assert.notEmpty(phoneNumbers, "phoneNumbers is emtpy");
        Assert.notEmpty(templateId, "templateId is emtpy");
        SendResult sendResult = SendResult.create();
        List<String> waitSendPhoneNumbers = new ArrayList<>(phoneNumbers.size());
        for (String phoneNumber : phoneNumbers) {
            SendResultData sendResultData = doInterceptor(phoneNumber, templateId, templateParam);
            if (sendResultData == null) {
                waitSendPhoneNumbers.add(phoneNumber);
            } else {
                sendResult.addSendResultData(sendResultData);
            }
        }
        SendResult doSendResult = this.smsExecutor.send(waitSendPhoneNumbers, templateId, templateParam);
        return sendResult.addSendResult(doSendResult);
    }

    @Override
    public SendResult send(List<String> phoneNumbers, List<String> templateIds, List<Map<String, String>> templateParams) {
        // 校验参数
        Assert.notEmpty(phoneNumbers, "phoneNumbers is emtpy");
        Assert.notEmpty(templateIds, "templateIds is emtpy");
        int sendSize = phoneNumbers.size();
        if (sendSize != templateIds.size()) {
            throw new IllegalArgumentException("phoneNumbers size not equals tempkateKeys size");
        }
        boolean emptyParams = CollectionUtils.isEmpty(templateParams);
        if (!emptyParams && templateParams.size() != sendSize) {
            throw new IllegalArgumentException("templateParams size not equals tempkateKeys size");
        }

        SendResult sendResult = SendResult.create();

        Iterator<String> phoneNumberIterator = phoneNumbers.iterator();
        Iterator<String> templateIdIterator = templateIds.iterator();
        // 模板参数可能为空
        Iterator<Map<String, String>> templateParamIterator = emptyParams ? null : templateParams.iterator();

        String phoneNumber, templateId = null;
        Map<String, String> templateParam = null;

        List<String> waitSendPhoneNumbers = new ArrayList<>(sendSize), waitSendtemplateIds = new ArrayList<>(sendSize);
        List<Map<String, String>> waitSendtemplateParams = new ArrayList<>(sendSize);

        while (phoneNumberIterator.hasNext()) {
            phoneNumber = phoneNumberIterator.next();
            templateId = templateIdIterator.next();
            templateParam = emptyParams ? null : templateParamIterator.next();

            SendResultData sendResultData = doInterceptor(phoneNumber, templateId, templateParam);
            if (sendResultData == null) {
                waitSendPhoneNumbers.add(phoneNumber);
                waitSendtemplateIds.add(templateId);
                if (!emptyParams) {
                    waitSendtemplateParams.add(templateParam);
                }
            } else {
                sendResult.addSendResultData(sendResultData);
            }
        }
        SendResult doSendResult = this.smsExecutor.send(waitSendPhoneNumbers, waitSendtemplateIds, waitSendtemplateParams);

        return sendResult.addSendResult(doSendResult);
    }
}
