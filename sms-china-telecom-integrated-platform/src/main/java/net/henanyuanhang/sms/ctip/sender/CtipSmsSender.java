package net.henanyuanhang.sms.ctip.sender;

import net.henanyuanhang.sms.core.exception.SmsSendException;
import net.henanyuanhang.sms.core.sender.SmsSender;
import net.henanyuanhang.sms.core.sender.result.SendResult;
import net.henanyuanhang.sms.core.util.Assert;
import net.henanyuanhang.sms.ctip.CtipProperties;
import net.henanyuanhang.sms.httpextension.factory.HttpClient5Factory;
import org.apache.hc.client5.http.CircularRedirectException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import java.util.List;
import java.util.Map;

/**
 * 中国电信综合短信业务管理平台短信发送
 */
public class CtipSmsSender implements SmsSender {

    private CtipProperties ctipProperties;
    private CloseableHttpClient closeableHttpClient;


    public CtipSmsSender(CtipProperties ctipProperties) {
        this(ctipProperties, HttpClient5Factory.createHttpClient());
    }

    public CtipSmsSender(CtipProperties ctipProperties, CloseableHttpClient closeableHttpClient) {
        this.ctipProperties = ctipProperties;
        this.closeableHttpClient = closeableHttpClient;
    }




    @Override
    public SendResult send(String phoneNumber, String templateKey, Map<String, String> templateParams) throws SmsSendException {
        return null;
    }

    @Override
    public SendResult send(List<String> phoneNumbers, String templateKey, Map<String, String> templateParams) throws SmsSendException {
        return null;
    }

    @Override
    public SendResult send(List<String> phoneNumbers, List<String> templateKeys, List<Map<String, String>> templateParams) throws SmsSendException {
        return null;
    }
}
