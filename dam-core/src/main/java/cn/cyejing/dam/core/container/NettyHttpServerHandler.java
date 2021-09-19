package cn.cyejing.dam.core.container;

import cn.cyejing.dam.core.context.DefaultExchange;
import cn.cyejing.dam.core.context.InternalExchange;
import cn.cyejing.dam.core.context.Response;
import cn.cyejing.dam.core.exception.ErrorResolverFactory;
import cn.cyejing.dam.core.filter.FilteringHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    private static FilteringHandler handler = new FilteringHandler();

    public NettyHttpServerHandler() {
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {

        if (message instanceof HttpRequest) {
            handlerRequest(ctx, (FullHttpRequest) message);
        } else {
            // never go this way, If so, it must be a bug
            log.error("#NettyHttpServerHandler.channelRead0# message type is not HttpRequest: {}", message);
            boolean release = ReferenceCountUtil.release(message);
            if (!release) {
                log.error("#NettyHttpServerHandler.channelRead# release fail, release : {}", false);
            }
        }
    }


    public void handlerRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        try {
            InternalExchange exchange = new DefaultExchange(ctx, request);
            handler.handler(exchange);
        } catch (Throwable e) {
            log.error("build exchange or init filterChain fail", e);
            Response response = ErrorResolverFactory.resolve(e);
            ctx.writeAndFlush(response.build()).addListener(ChannelFutureListener.CLOSE);
            if (!ReferenceCountUtil.release(request)) {
                log.error("release request fail");
            }
        }
    }



}
