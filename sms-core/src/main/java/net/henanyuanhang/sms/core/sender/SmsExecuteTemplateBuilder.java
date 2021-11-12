package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.core.interceptor.ParamInterceptorManager;
import net.henanyuanhang.sms.core.sender.result.SendResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SmsExecuteTemplateBuilder {

    private SmsExecutor smsExecutor;

    private ParamInterceptorManager paramInterceptorManager;

    private ExecutorService executorService;

    public static SmsExecuteTemplateBuilder create() {
        return new SmsExecuteTemplateBuilder();
    }

    public SmsExecuteTemplateBuilder smsExecutor(SmsExecutor smsExecutor) {
        this.smsExecutor = smsExecutor;
        return this;
    }

    public SmsExecuteTemplateBuilder paramInterceptorManager(ParamInterceptorManager paramInterceptorManager) {
        this.paramInterceptorManager = paramInterceptorManager;
        return this;
    }

    public SmsExecuteTemplateBuilder executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    private ExecutorService defaultExecutorService() {
        return new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                1000 * 60,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(50000),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "SMSAsyncSendExecutor_" + this.threadIndex.incrementAndGet());
                    }
                });
    }

    public SmsExecuteTemplate build() {
        if (this.paramInterceptorManager == null) {
            this.paramInterceptorManager = new ParamInterceptorManager();
        }

        if (this.executorService == null) {
            this.executorService = defaultExecutorService();
        }


        SmsExecutor executorProxy;
        if (this.smsExecutor != null) {
            executorProxy = new SmsExecutorProxy(this.smsExecutor, paramInterceptorManager);
        } else {
            executorProxy = new NullSmsExecutor();
        }


        SmsExecuteTemplate smsExecuteTemplate = new SmsExecuteTemplate(executorProxy, executorService);
        return smsExecuteTemplate;
    }

    class NullSmsExecutor implements SmsExecutor {

        @Override
        public SendResult send(String phoneNumber, String templateId, Map<String, String> templateParam) {
            throw new IllegalStateException("smsExecutor is null");
        }

        @Override
        public SendResult send(List<String> phoneNumbers, String templateId, Map<String, String> templateParam) {
            throw new IllegalStateException("smsExecutor is null");
        }

        @Override
        public SendResult send(List<String> phoneNumbers, List<String> templateIds, List<Map<String, String>> templateParams) {
            throw new IllegalStateException("smsExecutor is null");
        }
    }
}
