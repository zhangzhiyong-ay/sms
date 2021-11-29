package net.henanyuanhang.sms.spring;

import net.henanyuanhang.sms.core.interceptor.ParamInterceptorManager;
import net.henanyuanhang.sms.core.sender.SmsExecuteTemplate;
import net.henanyuanhang.sms.core.sender.SmsExecuteTemplateBuilder;
import net.henanyuanhang.sms.core.sender.SmsExecutor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@Import(SmsAutoConfiguration.SmsConfigurationImportSelector.class)
@AutoConfigureAfter(HtipSmsConfiguration.class)
public class SmsAutoConfiguration {

    @Bean(name = "smsExecutorService")
    @ConditionalOnMissingBean(name = "smsExecutorService")
    public ExecutorService executorService() {
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


    @Bean
    @ConditionalOnMissingBean(SmsExecuteTemplate.class)
    public SmsExecuteTemplate smsExecuteTemplate(
            SmsExecutor smsExecutor,
            ParamInterceptorManager paramInterceptorManager,
            ExecutorService smsExecutorService) {
        return SmsExecuteTemplateBuilder.create()
                .smsExecutor(smsExecutor)
                .paramInterceptorManager(paramInterceptorManager)
                .executorService(smsExecutorService)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(ParamInterceptorManager.class)
    public ParamInterceptorManager paramInterceptorManager() {
        return new ParamInterceptorManager();
    }

    static class SmsConfigurationImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            SmsType[] types = SmsType.values();
            String[] imports = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                imports[i] = SmsConfigurations.getConfigurationClass(types[i]);
            }
            return imports;
        }
    }
}
