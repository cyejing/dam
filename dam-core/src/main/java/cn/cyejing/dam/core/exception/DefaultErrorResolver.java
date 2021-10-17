package cn.cyejing.dam.core.exception;

import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.context.DefaultResponse;
import cn.cyejing.dam.core.context.Response;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultErrorResolver implements ErrorResolver {

    @Override
    public Response resolve(Throwable t) {
        if (t instanceof DamException) {
            ErrorCode errorCode = ((DamException) t).getErrorCode();
            log.warn("dam error :{}", errorCode);
            return buildResponse(errorCode);
        } else {
            log.error("unknown error", t);
        }
        return buildResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "internal server error");
    }

    private DefaultResponse buildResponse(HttpResponseStatus status, ErrorCode code, String message) {
        ErrorBody errorBody = new ErrorBody(status, code, message);
        return new DefaultResponse(status, JSONUtil.writeValueAsString(errorBody));
    }

    private DefaultResponse buildResponse(ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.valueOf(errorCode);
        ErrorBody errorBody = new ErrorBody(errorResponse.getStatus(), errorResponse.getErrorCode(), errorResponse.getMessage());
        return new DefaultResponse(errorResponse.getStatus(), JSONUtil.writeValueAsString(errorBody));
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
