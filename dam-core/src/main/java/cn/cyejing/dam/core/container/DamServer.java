package cn.cyejing.dam.core.container;

import cn.cyejing.dam.core.config.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DamServer {

    private final Config config;
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();
    private final DamContainer damContainer;

    public DamServer(Config config, DamContainer damContainer) {
        this.config = config;
        this.damContainer = damContainer;
    }


    public DamServer start(int port) {
        NettyHttpServerHandler nettyHttpServerHandler = new NettyHttpServerHandler();
        ServerBootstrap serverBootstrap = this.serverBootstrap.group(damContainer.getEventLoopGroupBoss(), damContainer.getEventLoopGroupWork())
                .channel(damContainer.useEPoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, 65535)
                .childOption(ChannelOption.SO_RCVBUF, 65535)
                .childOption(ChannelOption.SO_KEEPALIVE, false)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {

                        ch.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(config.getMaxContentLength()),
                                new HttpServerExpectContinueHandler(),
                                nettyHttpServerHandler
                        );
                    }
                });

        if (config.isNettyAllocator()) {
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }

        try {
            this.serverBootstrap.bind(port).sync();
            log.info(" <----------- Dam Server StartUp On Port :" + port + " ---------------> ");
        } catch (InterruptedException e1) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
        }
        return this;
    }


}
