package net.henanyuanhang.sms.htip.client;

import net.henanyuanhang.sms.httpextension.HttpResponse;

public class Response implements HttpResponse {
    private String code;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getErrorCode() {
        return this.code;
    }

    @Override
    public String getErrorMessage() {
        return this.description;
    }
}
