package cn.cyejing.dam.core.exception;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum ErrorResponse {

    NOT_FOUND_ROUTE(ErrorCode.NOT_FOUND_ROUTE, HttpResponseStatus.NOT_FOUND, "not found route"),
    NOT_FOUND_RESOURCE(ErrorCode.NOT_FOUND_RESOURCE, HttpResponseStatus.NOT_FOUND, "not found resource"),
    URI_ILLEGALITY(ErrorCode.URI_ILLEGALITY, HttpResponseStatus.BAD_REQUEST, "uri illegality"),
    FILTER_TAIL(ErrorCode.FILTER_TAIL, HttpResponseStatus.NOT_EXTENDED, "No more filters"),
    ;

    @Getter
    private ErrorCode errorCode;
    @Getter
    private HttpResponseStatus status;
    @Getter
    private String message;

    private final static Map<String, ErrorResponse> RESPONSE_MAP = new HashMap<>();

    ErrorResponse(ErrorCode errorCode, HttpResponseStatus status, String message) {
        this.errorCode = errorCode;
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse valueOf(ErrorCode errorCode) {
        return valueOf(errorCode.name());
    }
}
