package cn.cyejing.dam.core.context;

import io.netty.channel.ChannelHandlerContext;

public interface InternalExchange extends Exchange {

    ChannelHandlerContext getChannelHandlerContext();

    boolean isKeepAlive();
}
