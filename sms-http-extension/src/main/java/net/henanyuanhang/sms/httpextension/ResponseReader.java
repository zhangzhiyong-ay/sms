package net.henanyuanhang.sms.httpextension;

import net.henanyuanhang.sms.common.utils.JSONUtils;
import net.henanyuanhang.sms.common.utils.StringUtils;
import org.apache.hc.core5.http.HttpStatus;

public class ResponseReader<T extends HttpResponse> {

    private static final String NULL_STRING = "null";

    private Class<T> tClass;

    private int httpStatus;

    private String reasonPhrase;

    private T successResponse;

    private boolean success;

    private ErrorResponse errorResponse;

    public ResponseReader(Class<T> tClass, int httpStatus, String reasonPhrase) {
        this.tClass = tClass;
        this.httpStatus = httpStatus;
        this.success = HttpStatus.SC_SUCCESS == httpStatus;
        this.reasonPhrase = reasonPhrase;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T readSuccessResponse() {
        if (successResponse == null) {
            this.successResponse = JSONUtils.toObject(this.reasonPhrase, tClass);
        }
        return this.successResponse;
    }

    public ErrorResponse readErrorResponse() {
        if (errorResponse == null) {
            String errorCode = NULL_STRING;
            String errorMessage = NULL_STRING;
            if (StringUtils.isNotEmpty(reasonPhrase)) {
                T t = JSONUtils.toObject(this.reasonPhrase, tClass);
                errorCode = t.getErrorCode();
                errorMessage = t.getErrorMessage();
            }
            this.errorResponse = new ErrorResponse(this.httpStatus, errorCode, errorMessage);
        }
        return this.errorResponse;
    }

}
