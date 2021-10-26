package net.henanyuanhang.core.sender.result;

import java.util.List;

/**
 * 发送结果
 */
public class SmsSendResult {

    /**
     * 发送结果状态集合，每个手机号的发送结果对应一个状态
     */
    private List<SmsSendStatus> smsSendStatuses;

    /**
     * 发送结果原始信息
     */
    private String originalResult;

    public List<SmsSendStatus> getSmsSendStatuses() {
        return smsSendStatuses;
    }

    public String getOriginalResult() {
        return originalResult;
    }
}
