package cn.cyejing.dam.core.container;

import cn.cyejing.dam.core.config.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;


@Slf4j
public class NettyHttpServer {

    private final ServerBootstrap serverBootstrap;

    private final Config config;

    private final EventLoopGroup eventLoopGroupBoss;
    private final EventLoopGroup eventLoopGroupWork;
    private final boolean useEPool;


    public NettyHttpServer(Config config,
                           EventLoopGroup eventLoopGroupBoss,
                           EventLoopGroup eventLoopGroupWork,
                           boolean useEPool) {
        this.config = config;
        this.serverBootstrap = new ServerBootstrap();
        this.eventLoopGroupBoss = eventLoopGroupBoss;
        this.eventLoopGroupWork = eventLoopGroupWork;
        this.useEPool = useEPool;

    }

    public void start(int port) {
        NettyHttpServerHandler nettyHttpServerHandler = new NettyHttpServerHandler();
        ServerBootstrap childHandler =
                this.serverBootstrap.group(eventLoopGroupBoss, eventLoopGroupWork)
                        .channel(useEPool ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .childOption(ChannelOption.SO_SNDBUF, 65535)
                        .childOption(ChannelOption.SO_RCVBUF, 65535)
                        .childOption(ChannelOption.SO_KEEPALIVE, false)
                        .localAddress(new InetSocketAddress(port))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {

                                ch.pipeline().addLast(
                                        new HttpServerCodec(),
                                        new HttpObjectAggregator(config.getMaxContentLength()),
                                        new HttpServerExpectContinueHandler(),
                                        nettyHttpServerHandler
                                );
                            }
                        });

        if (config.isNettyAllocator()) {
            childHandler.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }

        try {
            this.serverBootstrap.bind().sync();
            log.info(" <----------- Dam Server StartUp On Port :" + port + " ---------------> ");
        } catch (InterruptedException e1) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
        }

    }

}
