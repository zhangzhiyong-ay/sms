package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.common.utils.Assert;
import net.henanyuanhang.sms.core.sender.result.SendResult;
import net.henanyuanhang.sms.core.sender.result.SendResultCallback;
import net.henanyuanhang.sms.core.sender.result.SendResultData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 短信发送模板对象
 */
public class SmsExecuteTemplate {

    private final SmsExecutor smsSender;

    private final ExecutorService asyncSenderExecutor;

    public SmsExecuteTemplate(SmsExecutor smsSender) {
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

    public SmsExecuteTemplate(SmsExecutor smsSender, ExecutorService asyncSenderExecutor) {
        this.smsSender = smsSender;
        this.asyncSenderExecutor = asyncSenderExecutor;
    }

    public ExecutorService getAsyncSenderExecutor() {
        return asyncSenderExecutor;
    }

    private SendResult send(SmsSendFunction sendFunction) {
        Assert.notNull(sendFunction, "sendFunction is null");
        return sendFunction.send(smsSender);
    }

    /**
     * 同步--单个发送短信
     *
     * @param phoneNumber 发送短信的手机号，不可为空
     * @param templateKey 短信模板关键字，不可为空
     * @return
     */
    public SendResult send(String phoneNumber, String templateKey) {
        return this.send(phoneNumber, templateKey, null);
    }

    /**
     * 同步--单个发送短信
     *
     * @param phoneNumber    发送短信的手机号，不可为空
     * @param templateKey    短信模板关键字，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    public SendResult send(String phoneNumber, String templateKey, Map<String, String> templateParams) {
        return send(smsSender -> smsSender.send(phoneNumber, templateKey, templateParams));
    }

    /**
     * 同步--批量发送-同一短信内容向不同手机号发送
     *
     * @param phoneNumbers 批量发送的手机号，不可为空，且长度最小为1
     * @param templateKey  短信模板关键字，不可为空
     * @return
     */
    public SendResult send(List<String> phoneNumbers, String templateKey) {
        return this.send(phoneNumbers, templateKey, null);
    }


    /**
     * 同步--批量发送-同一短信内容向不同手机号发送
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateKey    短信模板关键字，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    public SendResult send(List<String> phoneNumbers, String templateKey, Map<String, String> templateParams) {
        return send(smsSender -> smsSender.send(phoneNumbers, templateKey, templateParams));
    }

    /**
     * 同步--批量发送-向不同手机号发送不同短信内容
     * 要求手机号、短信模板关键字、短信模板参数字段个数相同，一一对应。
     *
     * @param phoneNumbers 批量发送的手机号，不可为空，且长度最小为1
     * @param templateKeys 短信模板关键字，不可为空，且长度要与 phoneNumbers 字段个数一致
     * @return
     */
    public SendResult send(List<String> phoneNumbers, List<String> templateKeys) {
        return this.send(phoneNumbers, templateKeys, null);
    }

    /**
     * 同步--批量发送-向不同手机号发送不同短信内容
     * 要求手机号、短信模板关键字、短信模板参数字段个数相同，一一对应。
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateKeys   短信模板关键字，不可为空，且长度要与 phoneNumbers 字段个数一致
     * @param templateParams 短信模板参数。如果所有模板都无参数时，可以将该参数设置为null，
     *                       否则需要与 phoneNumbers 字段个数相同。如其中一个模板无参数时，可对该下标的值设置为null。
     * @return
     */
    public SendResult send(List<String> phoneNumbers, List<String> templateKeys, List<Map<String, String>> templateParams) {
        return send(smsSender -> smsSender.send(phoneNumbers, templateKeys, templateParams));
    }

    private void asyncSend(SmsSendFunction smsSendFunction, SendResultCallback sendCallback) {
        Assert.notNull(smsSendFunction, "smsSendFunction is null");
        ExecutorService executor = getAsyncSenderExecutor();
        SendResultCallback proxy = createSendCallbackProxy(sendCallback);
        executor.submit(() -> {
            SendResult result = smsSendFunction.send(smsSender);
            result.resultCallback(proxy);
        });
    }

    /**
     * 异步--单个发送短信
     *
     * @param phoneNumber 发送短信的手机号，不可为空
     * @param templateKey 短信模板关键字，不可为空
     * @return
     */
    public void asyncSend(String phoneNumber, String templateKey, SendResultCallback sendCallback) {
        this.asyncSend(phoneNumber, templateKey, null, sendCallback);
    }

    /**
     * 异步--单个发送短信
     *
     * @param phoneNumber    发送短信的手机号，不可为空
     * @param templateKey    短信模板关键字，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    public void asyncSend(String phoneNumber, String templateKey, Map<String, String> templateParams, SendResultCallback sendCallback) {
        asyncSend(smsSender -> smsSender.send(phoneNumber, templateKey, templateParams), sendCallback);
    }

    /**
     * 异步--批量发送-同一短信内容向不同手机号发送
     *
     * @param phoneNumbers 批量发送的手机号，不可为空，且长度最小为1
     * @param templateKey  短信模板关键字，不可为空
     * @return
     */
    public void asyncSend(List<String> phoneNumbers, String templateKey, SendResultCallback sendCallback) {
        this.asyncSend(phoneNumbers, templateKey, null, sendCallback);
    }


    /**
     * 异步--批量发送-同一短信内容向不同手机号发送
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateKey    短信模板关键字，不可为空
     * @param templateParams 短信模板变量对应的参数。若模板无变量，则设置为null
     * @return
     */
    public void asyncSend(List<String> phoneNumbers, String templateKey, Map<String, String> templateParams, SendResultCallback sendCallback) {
        asyncSend(smsSender -> smsSender.send(phoneNumbers, templateKey, templateParams), sendCallback);
    }

    /**
     * 异步--批量发送-向不同手机号发送不同短信内容
     * 要求手机号、短信模板关键字、短信模板参数字段个数相同，一一对应。
     *
     * @param phoneNumbers 批量发送的手机号，不可为空，且长度最小为1
     * @param templateKeys 短信模板关键字，不可为空，且长度要与 phoneNumbers 字段个数一致
     * @return
     */
    public void asyncSend(List<String> phoneNumbers, List<String> templateKeys, SendResultCallback sendCallback) {
        this.asyncSend(phoneNumbers, templateKeys, null, sendCallback);
    }

    /**
     * 异步--批量发送-向不同手机号发送不同短信内容
     * 要求手机号、短信模板关键字、短信模板参数字段个数相同，一一对应。
     *
     * @param phoneNumbers   批量发送的手机号，不可为空，且长度最小为1
     * @param templateKeys   短信模板关键字，不可为空，且长度要与 phoneNumbers 字段个数一致
     * @param templateParams 短信模板参数。如果所有模板都无参数时，可以将该参数设置为null，
     *                       否则需要与 phoneNumbers 字段个数相同。如其中一个模板无参数时，可对该下标的值设置为null。
     * @return
     */
    public void asyncSend(List<String> phoneNumbers, List<String> templateKeys, List<Map<String, String>> templateParams, SendResultCallback sendCallback) {
        asyncSend(smsSender -> smsSender.send(phoneNumbers, templateKeys, templateParams), sendCallback);
    }

    private SendResultCallback createSendCallbackProxy(SendResultCallback sendCallback) {
        return new SendResultCallbackProxy(sendCallback);
    }

    class SendResultCallbackProxy implements SendResultCallback {

        private SendResultCallback sendCallback;

        public SendResultCallbackProxy(SendResultCallback sendCallback) {
            this.sendCallback = sendCallback;
        }


        @Override
        public void onSuccess(List<SendResultData> successData) {
            if (sendCallback != null) {
                sendCallback.onSuccess(successData);
            }
        }

        @Override
        public void onFail(List<SendResultData> failData) {
            if (sendCallback != null) {
                sendCallback.onFail(failData);
            }
        }
    }

}
