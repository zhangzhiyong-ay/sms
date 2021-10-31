package net.henanyuanhang.sms.ctip.client;

import net.henanyuanhang.sms.core.sender.result.SendResultData;
import net.henanyuanhang.sms.core.util.StringUtils;
import net.henanyuanhang.sms.ctip.CtipProperties;
import net.henanyuanhang.sms.ctip.util.MD5Utils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信客户端
 */
public class SmsClient {

    /**
     * 请求头内容格式
     */

    private CtipProperties ctipProperties;
    private CloseableHttpClient closeableHttpClient;
    private String phoneNumber;
    private String templateId;
    private Map<String, String> templateParams;

    public SmsClient(
            CtipProperties ctipProperties,
            CloseableHttpClient closeableHttpClient,
            String phoneNumber,
            String templateId,
            Map<String, String> templateParams) {
        this.ctipProperties = ctipProperties;
        this.closeableHttpClient = closeableHttpClient;
        this.phoneNumber = phoneNumber;
        this.templateId = templateId;
        this.templateParams = templateParams;
    }


    /**
     * 生成认证消息头
     *
     * @return
     */
    private String authorization() {

    }


    private SendResultData execute() {

        closeableHttpClient.execute()
    }

    private HttpPost createHttpPost() {
        HttpPost post = new HttpPost(ctipProperties.getSendUrl());
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("entendedCode", ctipProperties.getExtendedCode());
        StringEntity stringEntity = new StringEntity();

    }

    class RequestBody {
        private String mobile;
        private String extendedCode;
        private String templateId;
        private Map<String, String> templateParameter;

        public RequestBody(String mobile, String extendedCode, String templateId, Map<String, String> templateParameter) {
            this.mobile = mobile;
            this.extendedCode = extendedCode;
            this.templateId = templateId;
            this.templateParameter = templateParameter;
        }


        public String getMobile() {
            return mobile;
        }

        public String getExtendedCode() {
            return extendedCode;
        }

        public String getTemplateId() {
            return templateId;
        }

        public Map<String, String> getTemplateParameter() {
            return templateParameter;
        }
    }

}
