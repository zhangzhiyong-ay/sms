package net.henanyuanhang.sms.htip.client;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.common.utils.JSONUtils;
import net.henanyuanhang.sms.htip.HtipProperties;
import net.henanyuanhang.sms.htip.exception.HtipClientException;
import net.henanyuanhang.sms.htip.exception.HtipServerException;
import net.henanyuanhang.sms.htip.message.HtipCodeMessageHolder;
import net.henanyuanhang.sms.httpextension.ErrorResponse;
import net.henanyuanhang.sms.httpextension.ResponseReader;
import net.henanyuanhang.sms.httpextension.httpclient5.HttpClient5Factory;
import net.henanyuanhang.sms.httpextension.httpclient5.HttpClient5ResponseReaderFactory;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信客户端
 */
public class SmsClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(SmsClient.class);

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
     * http客户端
     */
    private CloseableHttpClient closeableHttpClient;

    private final AuthorizationHandler authorizationHandler;

    private CodeMessage codeMessage;

    private final String contentEncoding;

    public SmsClient(HtipProperties htipProperties) {
        this(htipProperties, HttpClient5Factory.createHttpClient(), HtipCodeMessageHolder.getInstance());
    }

    public SmsClient(
            HtipProperties htipProperties,
            CloseableHttpClient closeableHttpClient,
            CodeMessage codeMessage) {
        this.contentEncoding = "UTF-8";
        this.codeMessage = codeMessage;
        this.sendUrl = htipProperties.getSendUrl();
        this.appId = htipProperties.getAppId();
        this.appSecret1 = htipProperties.getAppSecret1();
        this.extendedCode = htipProperties.getExtendedCode();
        this.closeableHttpClient = closeableHttpClient;
        this.authorizationHandler = new AuthorizationHandler(appId, appSecret1);
    }

    public void setCloseableHttpClient(CloseableHttpClient closeableHttpClient) {
        this.closeableHttpClient = closeableHttpClient;
    }

    public void setCodeMessage(CodeMessage codeMessage) {
        this.codeMessage = codeMessage;
    }

    /**
     * 发送短信
     *
     * @param phoneNumber       接收短信的手机号
     * @param templateId        短信模板ID。在平台上添加短信模板生成的模板ID
     * @param templateParameter 短信模板参数
     * @return
     * @throws HtipClientException 当短信发送失败时抛出异常
     */
    public SmsSendResponse sendSms(String phoneNumber, String templateId, Map<String, String> templateParameter) throws HtipClientException {

        String authorization = authorizationHandler.createRequestAuthorization();
        String body = createBody(phoneNumber, templateId, templateParameter);

        HttpPost post = new HttpPost(this.sendUrl);
        StringEntity stringEntity = new StringEntity(body, ContentType.APPLICATION_JSON, this.contentEncoding, false);
        post.setEntity(stringEntity);
        post.setHeader("Authorization", authorization);

        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("authorization为 {}，请求body为： {}", authorization, body);
            }
            CloseableHttpResponse httpResponse = closeableHttpClient.execute(post);

            ResponseReader<SmsSendResponse> reader = HttpClient5ResponseReaderFactory.createReader(SmsSendResponse.class, httpResponse);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("请求结果为： {}", reader.getReasonPhrase());
            }
            if (reader.isSuccess()) {
                SmsSendResponse smsSendResponse = reader.readSuccessResponse();
                smsSendResponse.setDescription(codeMessage.getMessage(smsSendResponse.getCode(), "短信发送成功"));
                return smsSendResponse;
            } else {
                ErrorResponse errorResponse = reader.readErrorResponse();
                throw new HtipServerException(errorResponse.getErrorCode(), codeMessage.getMessage(errorResponse.getErrorCode(), "短信发送服务异常"), errorResponse.getHttpStatus());
            }

        } catch (IOException e) {
            throw new HtipClientException("COMMON_SERVICE_ERROR", codeMessage.getMessage("COMMON_SERVICE_ERROR"), e);
        } catch (ParseException e) {
            throw new HtipClientException("COMMON_CLIENT_RESPONSE_PARSE_ERROR", codeMessage.getMessage("COMMON_CLIENT_RESPONSE_PARSE_ERROR"), e);
        }
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
