package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.core.sender.result.SendResult;

import java.util.Objects;

class SendCallbackProxy implements SendCallback {

    private SendCallback sendCallback;

    public SendCallbackProxy(SendCallback sendCallback) {
        this.sendCallback = sendCallback;
    }

    @Override
    public void onSuccess(SendResult sendResult) {
        if (Objects.nonNull(sendCallback)) {
            sendCallback.onSuccess(sendResult);
        }
    }

    @Override
    public void onException(Throwable e) {
        if (Objects.nonNull(sendCallback)) {
            sendCallback.onException(e);
        }
    }
}
