package net.henanyuanhang.sms.core.sender.result;

import net.henanyuanhang.sms.common.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送结果
 */
public class SendResult {

    /**
     * 发送结果状态集合，每个手机号的发送结果对应一个状态
     */
    private List<SendResultData> successData;

    private List<SendResultData> failData;

    public SendResult(List<SendResultData> successData, List<SendResultData> failData) {
        this.successData = successData;
        this.failData = failData;
    }

    public void resultCallback(SendResultCallback callback) {
        if (CollectionUtils.notEmpty(successData)) {
            callback.onSuccess(successData);
        }
        if (CollectionUtils.notEmpty(failData)) {
            callback.onFail(failData);
        }
    }

    public List<SendResultData> getSuccessData() {
        return successData;
    }

    public void setSuccessData(List<SendResultData> successData) {
        this.successData = successData;
    }

    public List<SendResultData> getFailData() {
        return failData;
    }

    public void setFailData(List<SendResultData> failData) {
        this.failData = failData;
    }

    public static SendResultBuilder builder() {
        return new SendResultBuilder();
    }

    public static class SendResultBuilder {
        private List<SendResultData> successData = new ArrayList<>();
        private List<SendResultData> failData = new ArrayList<>();

        public SendResultBuilder addSendData(SendResultData sendResultData) {
            if (null != sendResultData) {
                if (sendResultData.isSuccess()) {
                    successData.add(sendResultData);
                } else {
                    failData.add(sendResultData);
                }
            }
            return this;
        }

        public SendResult build() {
            return new SendResult(successData, failData);
        }
    }

}
