package net.henanyuanhang.sms.spring;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.core.sender.SmsExecutor;
import net.henanyuanhang.sms.htip.message.HtipCodeMessageFactory;
import net.henanyuanhang.sms.htip.profile.HtipProfile;
import net.henanyuanhang.sms.htip.sender.HtipSmsExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(HtipSmsExecutor.class)
@ConditionalOnMissingBean(SmsExecutor.class)
@ConditionalOnProperty(value = "yuanhang.sms.type", havingValue = "htip")
public class HtipSmsConfiguration {

    private SmsProperties smsProperties;

    public HtipSmsConfiguration(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Bean
    public SmsExecutor smsExecutor(CodeMessage codeMessage) {
        SmsProperties.Htip htip = smsProperties.getHtip();
        HtipProfile htipProfile = new HtipProfile();
        htipProfile.setSendUrl(htip.getSendUrl());
        htipProfile.setAppId(htip.getAppId());
        htipProfile.setAppSecret1(htip.getAppSecret1());
        htipProfile.setExtendedCode(htip.getExtendedCode());
        return new HtipSmsExecutor(htipProfile, codeMessage);
    }

    @Bean
    public CodeMessage codeMessage() {
        return new HtipCodeMessageFactory().create();
    }
}
