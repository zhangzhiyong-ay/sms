package net.henanyuanhang.sms.core.sender.result;

import java.util.List;

/**
 * 发送回调函数
 */
public interface SendResultCallback {

    void onSuccess(final List<SendResultData> successData);

    void onFail(final List<SendResultData> failData);
}
