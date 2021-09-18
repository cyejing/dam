package cn.cyejing.dam.core.exception;

import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.context.DefaultResponse;
import cn.cyejing.dam.core.context.Response;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;

public class DefaultErrorResolver implements ErrorResolver {

    @Override
    public Response resolve(Throwable t) {
        if (t instanceof DamException) {
            ErrorCode errorCode = ((DamException) t).getErrorCode();
            switch (errorCode) {
                case NOT_FOUND: return buildResponse(HttpResponseStatus.NOT_FOUND, errorCode, "not found route");
                case FILTER_TAIL: return buildResponse(HttpResponseStatus.NOT_EXTENDED, errorCode, "No more filters");
                case INTERNAL_SERVER_ERROR: return buildResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, errorCode, "internal server error");
            }
        }else{
            return buildResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "internal server error");
        }
        return null;
    }

    private DefaultResponse buildResponse(HttpResponseStatus status, ErrorCode code, String message) {
        ErrorBody errorBody = new ErrorBody(status, code, message);
        return new DefaultResponse(status, JSONUtil.toJSONString(errorBody));
    }

    @Data
    public static class ErrorBody {
        private int status;
        private String reason;
        private String code;
        private String message;

        public ErrorBody(HttpResponseStatus status, ErrorCode code, String message) {
            this.status = status.code();
            this.reason = status.reasonPhrase();
            this.code = code.name();
            this.message = message;
        }

        public ErrorBody(int status, String reason, String code, String message) {
            this.status = status;
            this.reason = reason;
            this.code = code;
            this.message = message;
        }
    }

}
