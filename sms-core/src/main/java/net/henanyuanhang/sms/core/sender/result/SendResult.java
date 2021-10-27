package net.henanyuanhang.sms.core.sender.result;

import java.util.List;

/**
 * 发送结果
 */
public class SendResult {

    /**
     * 发送结果状态集合，每个手机号的发送结果对应一个状态
     */
    private List<SendResultData> smsSendDatas;

    /**
     * 发送结果原始信息
     */
    private String originalResult;

    public void setOriginalResult(String originalResult) {
        this.originalResult = originalResult;
    }

    public void setSmsSendDatas(List<SendResultData> smsSendDatas) {
        this.smsSendDatas = smsSendDatas;
    }

    public List<SendResultData> getSmsSendDatas() {
        return smsSendDatas;
    }

    public String getOriginalResult() {
        return originalResult;
    }
}
