package cn.cyejing.dam.core.context;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicBoolean;

public interface InternalExchange extends Exchange {

    ChannelHandlerContext getChannelHandlerContext();

    boolean isKeepAlive();

    AtomicBoolean getWritten();
}
