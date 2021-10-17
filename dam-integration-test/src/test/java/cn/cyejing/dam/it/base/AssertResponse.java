package cn.cyejing.dam.it.base;

import cn.cyejing.dam.common.utils.JSONUtil;
import cn.cyejing.dam.core.exception.ErrorCode;
import cn.cyejing.dam.core.exception.ErrorResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.asynchttpclient.Response;
import org.junit.Assert;

public class AssertResponse {

    public static void assertEquals(ErrorCode errorCode, Response response) {
        ErrorResponse errorResponse = ErrorResponse.valueOf(errorCode);
        JsonNode body = JSONUtil.readValue(response.getResponseBody());
        Assert.assertEquals(errorResponse.getStatus().code(), response.getStatusCode());
        Assert.assertEquals(errorResponse.getStatus().code(), body.get("status").asInt());
        Assert.assertEquals(errorResponse.getStatus().reasonPhrase(), body.get("reason").asText());
        Assert.assertEquals(errorResponse.getErrorCode().name(),body.get("code").asText());
        Assert.assertEquals(errorResponse.getMessage(),body.get("message").asText());

    }
}
