package cn.cyejing.dam.core.utils;

import cn.cyejing.dam.common.config.Route;
import cn.cyejing.dam.core.context.DefaultExchange;
import cn.cyejing.dam.core.context.DefaultRequest;
import cn.cyejing.dam.core.context.Exchange;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.mockito.Mockito;


public class Utils {

    public static Exchange buildExchange(Route route, DefaultRequest request) {
        return new DefaultExchange(Mockito.mock(ChannelHandlerContext.class), Mockito.mock(FullHttpRequest.class),
                false, request, route);
    }


    public static Exchange buildExchange(DefaultRequest request) {
        Route route = new Route();
        route.setGroup("test");
        return buildExchange(route, request);
    }

}
