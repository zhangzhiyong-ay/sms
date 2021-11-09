package net.henanyuanhang.sms.htip.client;

public class SmsSendResponse extends Response {
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
