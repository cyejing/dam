package cn.cyejing.dam.core.exception;

import cn.cyejing.dam.core.context.Response;

public interface ErrorResolver {

    Response resolve(Throwable t);
}
