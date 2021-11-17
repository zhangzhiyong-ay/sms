package net.henanyuanhang.sms.htip.message;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.common.message.CodeMessageProxy;
import net.henanyuanhang.sms.common.utils.MapUtils;

import java.util.HashMap;
import java.util.Map;

public class HtipCodeMessageFactory {

    private CodeMessageProxy codeMessageProxy;

    private static Map<String, String> DEFAULT_MESSAGE_MAPPING = new HashMap<>();

    private Map<String, String> customMessageMapping;

    static {
        DEFAULT_MESSAGE_MAPPING.put("0000000", "发送成功");
        DEFAULT_MESSAGE_MAPPING.put("0000001", "短信服务内部错误");
        DEFAULT_MESSAGE_MAPPING.put("0000002", "通用请求消息参数错误");
        DEFAULT_MESSAGE_MAPPING.put("0001101", "客户ID不存在");
        DEFAULT_MESSAGE_MAPPING.put("0001102", "客户状态异常");
        DEFAULT_MESSAGE_MAPPING.put("0001103", "密码错误");
        DEFAULT_MESSAGE_MAPPING.put("0001104", "能力API鉴权失败");
        DEFAULT_MESSAGE_MAPPING.put("0001105", "应用ID不存在");
        DEFAULT_MESSAGE_MAPPING.put("0001106", "应用状态异常");
        DEFAULT_MESSAGE_MAPPING.put("0001109", "客户处于黑名单中");
        DEFAULT_MESSAGE_MAPPING.put("0001110", "IP不在白名单中");
        DEFAULT_MESSAGE_MAPPING.put("0001113", "认证鉴权失败");
        DEFAULT_MESSAGE_MAPPING.put("0002002", "号码格式错误");
        DEFAULT_MESSAGE_MAPPING.put("1001001", "流量控制失败");
        DEFAULT_MESSAGE_MAPPING.put("1001012", "流控限制");
        DEFAULT_MESSAGE_MAPPING.put("E000003", "模板请求参数类型错误");
        DEFAULT_MESSAGE_MAPPING.put("E000004", "模板请求参数个数不匹配");
        DEFAULT_MESSAGE_MAPPING.put("E000005", "模板请求ID错误");
        DEFAULT_MESSAGE_MAPPING.put("E000006", "模板请求参数存在敏感词");
        DEFAULT_MESSAGE_MAPPING.put("0000006", "时间段、模板配额、周末发送限制");
        DEFAULT_MESSAGE_MAPPING.put("0000007", "组装短信内容失败");
        DEFAULT_MESSAGE_MAPPING.put("0000008", "短信长度超过70");
        DEFAULT_MESSAGE_MAPPING.put("0000009", "号码在红名单或黑名单里");
        DEFAULT_MESSAGE_MAPPING.put("0000010", "没有通道");
        DEFAULT_MESSAGE_MAPPING.put("0000011", "押金不足");
        DEFAULT_MESSAGE_MAPPING.put("0000012", "发送失败");
        DEFAULT_MESSAGE_MAPPING.put("0000014", "重复订阅");
        DEFAULT_MESSAGE_MAPPING.put("0000015", "未知错误");
        DEFAULT_MESSAGE_MAPPING.put("0000016", "重复取消订阅");
        DEFAULT_MESSAGE_MAPPING.put("0000017", "此号没有订阅");
    }

    public HtipCodeMessageFactory() {
        this(null);
    }

    public HtipCodeMessageFactory(Map<String, String> customMessageMapping) {
        this.customMessageMapping = customMessageMapping;
    }

    public CodeMessage create() {
        int size = MapUtils.size(DEFAULT_MESSAGE_MAPPING) + MapUtils.size(customMessageMapping);
        Map<String, String> messageMapping = new HashMap<>(size);
        messageMapping.putAll(DEFAULT_MESSAGE_MAPPING);
        if (MapUtils.notEmpty(customMessageMapping)) {
            messageMapping.putAll(customMessageMapping);
        }
        return new CodeMessageProxy(messageMapping);
    }


}
