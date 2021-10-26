package net.henanyuanhang.core.config;

import java.util.Map;

/**
 * 配置文件
 */
public abstract class SmsConfig {

    /**
     * 短信模板，key为模板标识，value为短信模板内容
     */
    private Map<String, String> templates;

    /**
     * 短信签名
     */
    private String signName;

    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
}
