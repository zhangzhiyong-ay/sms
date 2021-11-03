package net.henanyuanhang.sms.ctip.client;

public class CtipSmsSendResponse extends CtipResponse {
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
