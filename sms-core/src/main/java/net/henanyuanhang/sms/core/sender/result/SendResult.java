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
        this.successData = CollectionUtils.isEmpty(successData) ? new ArrayList<>() : successData;
        this.failData = CollectionUtils.isEmpty(failData) ? new ArrayList<>() : failData;
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

    public SendResult addSendResultData(SendResultData sendResultData) {
        if (sendResultData.isSuccess()) {
            successData.add(sendResultData);
        } else {
            failData.add(sendResultData);
        }
        return this;
    }

    public SendResult addSendResult(SendResult sendResult) {
        successData.addAll(sendResult.getSuccessData());
        failData.addAll(sendResult.getFailData());
        return this;
    }

    public static SendResult create() {
        return new SendResult(null, null);
    }

}
