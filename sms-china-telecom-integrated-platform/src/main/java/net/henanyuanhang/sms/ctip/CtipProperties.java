package net.henanyuanhang.sms.ctip;

import java.util.Map;

public class CtipProperties {

    /**
     * 发送短信请求接口地址
     * 必填项
     */
    private String sendUrl;

    /**
     * 接口应用标识
     * 可在我的信息-业务信息中查看该值内容
     * 必填项
     */
    private String appId;

    /**
     * 调用接口秘钥
     * 可在我的信息-业务信息中查看该值内容
     * 必填项
     */
    private String appSecret1;

    /**
     * 扩展码
     * 可在我的信息-业务信息中查看该值内容
     * 必填项
     */
    private String extendedCode;

    /**
     * 短信模板，key值为自定义值，value为平台上添加的短信模板ID
     * 必填项
     */
    private Map<String, String> template;

    public Map<String, String> getTemplate() {
        return template;
    }

    public void setTemplate(Map<String, String> template) {
        this.template = template;
    }

    public String getSendUrl() {
        return sendUrl;
    }

    public void setSendUrl(String sendUrl) {
        this.sendUrl = sendUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret1() {
        return appSecret1;
    }

    public void setAppSecret1(String appSecret1) {
        this.appSecret1 = appSecret1;
    }

    public String getExtendedCode() {
        return extendedCode;
    }

    public void setExtendedCode(String extendedCode) {
        this.extendedCode = extendedCode;
    }
}
