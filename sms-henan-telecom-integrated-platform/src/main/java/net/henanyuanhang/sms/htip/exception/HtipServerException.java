package net.henanyuanhang.sms.htip.exception;

import net.henanyuanhang.sms.httpextension.ErrorResponse;

public class HtipServerException extends HtipClientException {

    private int httpStatus;

    public HtipServerException(String code, String message, int httpStatus) {
        super(code, message, null);
        this.httpStatus = httpStatus;
    }

    public HtipServerException(ErrorResponse errorResponse) {
        super(errorResponse.getErrorCode(), errorResponse.getErrorMessage());
        this.httpStatus = errorResponse.getHttpStatus();
    }
}
