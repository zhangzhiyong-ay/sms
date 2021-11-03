package net.henanyuanhang.sms.ctip.exception;

import net.henanyuanhang.sms.httpextension.ErrorResponse;

public class CtipServerException extends CtipClientException {

    private int httpStatus;

    public CtipServerException(String code, String message, int httpStatus) {
        super(code, message, null);
        this.httpStatus = httpStatus;
    }

    public CtipServerException(ErrorResponse errorResponse) {
        super(errorResponse.getErrorCode(), errorResponse.getErrorMessage());
        this.httpStatus = errorResponse.getHttpStatus();
    }
}
