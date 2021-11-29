package net.henanyuanhang.sms.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yuanhang.sms")
public class SmsProperties {

    private SmsType type;

    private final Htip htip = new Htip();

    public SmsType getType() {
        return type;
    }

    public void setType(SmsType type) {
        this.type = type;
    }

    public Htip getHtip() {
        return htip;
    }

    /**
     * 河南电信综合短信平台短信配置
     */
    public static class Htip {
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
}
