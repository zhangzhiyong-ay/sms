package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.core.sender.result.SendResult;

/**
 * 发送回调函数
 */
public interface SendCallback {

    void onSuccess(final SendResult sendResult);

    void onException(final Throwable e);
}
