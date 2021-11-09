package net.henanyuanhang.sms.htip.client;

import net.henanyuanhang.sms.common.utils.StringUtils;
import net.henanyuanhang.sms.htip.util.MD5Utils;

import java.nio.charset.StandardCharsets;

/**
 * 消息头认证处理器
 */
class AuthorizationHandler {

    private String appId;

    private String appSecret1;

    private final String authorizationFormat = "EOPAUTH appid=\"{}\",timestamp=\"{}\",signature=\"{}\"";

    protected AuthorizationHandler(String appId, String appSecret1) {
        this.appId = appId;
        this.appSecret1 = appSecret1;
    }

    /**
     * 创建请求调用接口时的 authorization消息头值
     * @return
     */
    public String createRequestAuthorization() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String signature = MD5Utils.md5Hex(timestamp + this.appSecret1, StandardCharsets.UTF_8).substring(16);
        return StringUtils.format(authorizationFormat, this.appId, timestamp, signature);
    }
}
