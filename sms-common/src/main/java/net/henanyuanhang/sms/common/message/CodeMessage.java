package net.henanyuanhang.sms.common.message;

public interface CodeMessage {

    String getMessage(String code);

    String getMessage(String code, String defaultMessage);
}
