package cn.cyejing.dam.core.exception;

import cn.cyejing.dam.core.context.DefaultResponse;
import cn.cyejing.dam.core.context.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@Slf4j
public  class ErrorResolverFactory {

    private static ErrorResolverFactory INSTANCE = new ErrorResolverFactory();

    private List<ErrorResolver> errorResolvers = new ArrayList<>();

    private ErrorResolverFactory() {
        ServiceLoader<ErrorResolver> loader = ServiceLoader.load(ErrorResolver.class);
        for (ErrorResolver errorResolver : loader) {
            errorResolvers.add(errorResolver);
        }
    }

    public static Response resolve(Throwable t) {
        for (ErrorResolver errorResolver : INSTANCE.errorResolvers) {
            try {
                Response response = errorResolver.resolve(t);
                if (response != null) {
                    return response;
                }
            } catch (Exception e) {
                log.error("resolver exception occur error", e);
            }
        }
        return new DefaultResponse("no error resolver");
    }
}
