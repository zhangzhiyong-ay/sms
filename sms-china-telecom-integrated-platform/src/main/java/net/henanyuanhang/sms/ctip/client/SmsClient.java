package net.henanyuanhang.sms.ctip.client;

import net.henanyuanhang.sms.common.ErrorCodeMessage;
import net.henanyuanhang.sms.common.utils.JSONUtils;
import net.henanyuanhang.sms.common.utils.StringUtils;
import net.henanyuanhang.sms.core.sender.result.SendResultData;
import net.henanyuanhang.sms.ctip.CtipProperties;
import net.henanyuanhang.sms.ctip.exception.CtipClientException;
import net.henanyuanhang.sms.ctip.exception.CtipServerException;
import net.henanyuanhang.sms.httpextension.ErrorResponse;
import net.henanyuanhang.sms.httpextension.ResponseReader;
import net.henanyuanhang.sms.httpextension.httpclient5.HttpClient5Factory;
import net.henanyuanhang.sms.httpextension.httpclient5.HttpClient5ResponseReaderFactory;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信客户端
 */
public class SmsClient {

    /**
     * 发送短信请求接口地址
     * 必填项
     */
    private final String sendUrl;

    /**
     * 接口应用标识
     * 可在我的信息-业务信息中查看该值内容
     * 必填项
     */
    private final String appId;

    /**
     * 调用接口秘钥
     * 可在我的信息-业务信息中查看该值内容
     * 必填项
     */
    private final String appSecret1;

    /**
     * 扩展码
     * 可在我的信息-业务信息中查看该值内容
     * 必填项
     */
    private final String extendedCode;

    /**
     * 短信模板，key值为自定义值，value为平台上添加的短信模板ID
     * 必填项
     */
    private final Map<String, String> template;

    /**
     * http客户端
     */
    private final CloseableHttpClient closeableHttpClient;

    private final AuthorizationHandler authorizationHandler;

    private ErrorCodeMessage errorCodeMessage;

    private String contentEncoding;


    public SmsClient(CtipProperties ctipProperties) {
        this.contentEncoding = "UTF-8";
        this.errorCodeMessage = ErrorCodeMessage.getInstance();
        this.sendUrl = ctipProperties.getSendUrl();
        this.appId = ctipProperties.getAppId();
        this.appSecret1 = ctipProperties.getAppSecret1();
        this.extendedCode = ctipProperties.getExtendedCode();
        this.template = ctipProperties.getTemplate();
        this.closeableHttpClient = HttpClient5Factory.createHttpClient();
        this.authorizationHandler = new AuthorizationHandler(appId, appSecret1);
    }

    public String getSendUrl() {
        return sendUrl;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret1() {
        return appSecret1;
    }

    public String getExtendedCode() {
        return extendedCode;
    }

    public Map<String, String> getTemplate() {
        return template;
    }

    public CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    public CtipSmsSendResponse sendSms(String phoneNumber, String templateKey, Map<String, String> templateParameter) throws CtipClientException {

        String templateId = template.get(templateKey);
        if (StringUtils.isEmpty(templateId)) {
            throw new CtipClientException("COMMON_TEMPLATE_ID_ILLEGAL", errorCodeMessage.getMessage("COMMON_TEMPLATE_ID_ILLEGAL"));
        }

        SendResultData sendResultData = null;

        String authorization = authorizationHandler.createRequestAuthorization();
        String body = createBody(phoneNumber, templateId, templateParameter);

        HttpPost post = new HttpPost(this.sendUrl);

        StringEntity stringEntity = new StringEntity(body, ContentType.APPLICATION_JSON, this.contentEncoding, false);
        post.setEntity(stringEntity);
        post.setHeader("Authorization", authorization);

        try {
            CloseableHttpResponse httpResponse = closeableHttpClient.execute(post);
            ResponseReader<CtipSmsSendResponse> reader = HttpClient5ResponseReaderFactory.createReader(CtipSmsSendResponse.class, httpResponse);
            if (reader.isSuccess()) {
                CtipSmsSendResponse smsSendResponse = reader.readSuccessResponse();
                return smsSendResponse;
            } else {
                ErrorResponse errorResponse = reader.readErrorResponse();
                throw new CtipServerException(errorResponse);
            }

        } catch (IOException e) {
            throw new CtipClientException("COMMON_SERVICE_ERROR", errorCodeMessage.getMessage("COMMON_SERVICE_ERROR"), e);
        } catch (ParseException e) {
            throw new CtipClientException("COMMON_CLIENT_RESPONSE_PARSE_ERROR", errorCodeMessage.getMessage("COMMON_CLIENT_RESPONSE_PARSE_ERROR"), e);
        }
    }

    private <T> T getResponse(CloseableHttpResponse response, Class<T> tClass) throws Exception {
        String json = EntityUtils.toString(response.getEntity());
        return JSONUtils.toObject(json, tClass);
    }

    private String createBody(String mobile, String templateId, Map<String, String> templateParameter) {
        return new RequestBody(mobile, this.extendedCode, templateId, templateParameter).toJson();
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
            this.templateParameter = templateParameter == null ? new HashMap<>() : templateParameter;
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

        public String toJson() {
            return JSONUtils.toString(this);
        }

    }

}
