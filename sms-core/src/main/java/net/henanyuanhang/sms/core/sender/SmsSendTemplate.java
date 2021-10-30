package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.core.exception.SmsSendException;
import net.henanyuanhang.sms.core.sender.result.SendResult;
import net.henanyuanhang.sms.core.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 短信发送模板对象
 */
public class SmsSendTemplate {

    private final SmsSender smsSender;

    private final ExecutorService asyncSenderExecutor;

    public SmsSendTemplate(SmsSender smsSender) {
        this.smsSender = smsSender;
        this.asyncSenderExecutor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                1000 * 60,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(50000),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "SMSAsyncSenderExecutor_" + this.threadIndex.incrementAndGet());
                    }
                });
    }

    public SmsSendTemplate(SmsSender smsSender, ExecutorService asyncSenderExecutor) {
        this.smsSender = smsSender;
        this.asyncSenderExecutor = asyncSenderExecutor;
    }

    public ExecutorService getAsyncSenderExecutor() {
        return asyncSenderExecutor;
    }

    private SendResult send(SmsSendFunction sendFunction) {
        Assert.notNull(sendFunction, "sendFunction is null");
        try {
            return sendFunction.send(smsSender);
        } catch (SmsSendException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 同步--单个发送短信
     *
     * @param phoneNumber 发送短信的手机号，不可为空
     * @param templateId  短信模板标识，不可为空
     * @return
     */
    public SendResult send(String phoneNumber, String templateId) {
        return this.send(phoneNumber, templateId, null);
    }

    /**
     * 同步--单个发送短信
     *
     * @param phoneNumber    发送短信的手机号，不可为空
     * @param templateId     短信模板标识，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    public SendResult send(String phoneNumber, String templateId, Map<String, String> templateParams) {
        return send(smsSender -> smsSender.send(phoneNumber, templateId, templateParams));
    }

    /**
     * 同步--批量发送-同一短信内容向不同手机号发送
     *
     * @param phoneNumbers 批量发送的手机号，不可为空，且长度最小为1
     * @param templateId   短信模板标识，不可为空
     * @return
     */
    public SendResult send(List<String> phoneNumbers, String templateId) {
        return this.send(phoneNumbers, templateId, null);
    }


    /**
     * 同步--批量发送-同一短信内容向不同手机号发送
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateId     短信模板标识，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    public SendResult send(List<String> phoneNumbers, String templateId, Map<String, String> templateParams) {
        return send(smsSender -> smsSender.send(phoneNumbers, templateId, templateParams));
    }

    /**
     * 同步--批量发送-向不同手机号发送不同短信内容
     * 要求手机号、短信模板标识、短信模板参数字段个数相同，一一对应。
     *
     * @param phoneNumbers 批量发送的手机号，不可为空，且长度最小为1
     * @param templateIds  短信模板标识，不可为空，且长度要与 phoneNumbers 字段个数一致
     * @return
     */
    public SendResult send(List<String> phoneNumbers, List<String> templateIds) {
        return this.send(phoneNumbers, templateIds, null);
    }

    /**
     * 同步--批量发送-向不同手机号发送不同短信内容
     * 要求手机号、短信模板标识、短信模板参数字段个数相同，一一对应。
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateIds    短信模板标识，不可为空，且长度要与 phoneNumbers 字段个数一致
     * @param templateParams 短信模板参数。如果所有模板都无参数时，可以将该参数设置为null，
     *                       否则需要与 phoneNumbers 字段个数相同。如其中一个模板无参数时，可对该下标的值设置为null。
     * @return
     */
    public SendResult send(List<String> phoneNumbers, List<String> templateIds, List<Map<String, String>> templateParams) {
        return send(smsSender -> smsSender.send(phoneNumbers, templateIds, templateParams));
    }

    private void asyncSend(SmsSendFunction smsSendFunction, SendCallback sendCallback) {
        Assert.notNull(smsSendFunction, "smsSendFunction is null");
        ExecutorService executor = getAsyncSenderExecutor();
        SendCallback proxy = createSendCallbackProxy(sendCallback);
        executor.submit(() -> {
            try {
                SendResult result = smsSendFunction.send(smsSender);
                proxy.onSuccess(result);
            } catch (Exception e) {
                proxy.onException(e);
            }
        });
    }

    /**
     * 异步--单个发送短信
     *
     * @param phoneNumber 发送短信的手机号，不可为空
     * @param templateId  短信模板标识，不可为空
     * @return
     */
    public void asyncSend(String phoneNumber, String templateId, SendCallback sendCallback) {
        this.asyncSend(phoneNumber, templateId, null, sendCallback);
    }

    /**
     * 异步--单个发送短信
     *
     * @param phoneNumber    发送短信的手机号，不可为空
     * @param templateId     短信模板标识，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    public void asyncSend(String phoneNumber, String templateId, Map<String, String> templateParams, SendCallback sendCallback) {
        asyncSend(smsSender -> smsSender.send(phoneNumber, templateId, templateParams), sendCallback);
    }

    /**
     * 异步--批量发送-同一短信内容向不同手机号发送
     *
     * @param phoneNumbers 批量发送的手机号，不可为空，且长度最小为1
     * @param templateId   短信模板标识，不可为空
     * @return
     */
    public void asyncSend(List<String> phoneNumbers, String templateId, SendCallback sendCallback) {
        this.asyncSend(phoneNumbers, templateId, null, sendCallback);
    }


    /**
     * 异步--批量发送-同一短信内容向不同手机号发送
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateId     短信模板标识，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    public void asyncSend(List<String> phoneNumbers, String templateId, Map<String, String> templateParams, SendCallback sendCallback) {
        asyncSend(smsSender -> smsSender.send(phoneNumbers, templateId, templateParams), sendCallback);
    }

    /**
     * 异步--批量发送-向不同手机号发送不同短信内容
     * 要求手机号、短信模板标识、短信模板参数字段个数相同，一一对应。
     *
     * @param phoneNumbers 批量发送的手机号，不可为空，且长度最小为1
     * @param templateIds  短信模板标识，不可为空，且长度要与 phoneNumbers 字段个数一致
     * @return
     */
    public void asyncSend(List<String> phoneNumbers, List<String> templateIds, SendCallback sendCallback) {
        this.asyncSend(phoneNumbers, templateIds, null, sendCallback);
    }

    /**
     * 异步--批量发送-向不同手机号发送不同短信内容
     * 要求手机号、短信模板标识、短信模板参数字段个数相同，一一对应。
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateIds    短信模板标识，不可为空，且长度要与 phoneNumbers 字段个数一致
     * @param templateParams 短信模板参数。如果所有模板都无参数时，可以将该参数设置为null，
     *                       否则需要与 phoneNumbers 字段个数相同。如其中一个模板无参数时，可对该下标的值设置为null。
     * @return
     */
    public void asyncSend(List<String> phoneNumbers, List<String> templateIds, List<Map<String, String>> templateParams, SendCallback sendCallback) {
        asyncSend(smsSender -> smsSender.send(phoneNumbers, templateIds, templateParams), sendCallback);
    }

    private SendCallback createSendCallbackProxy(SendCallback sendCallback) {
        return new SendCallbackProxy(sendCallback);
    }

}
