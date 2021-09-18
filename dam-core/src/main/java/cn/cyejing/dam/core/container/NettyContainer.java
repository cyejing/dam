package cn.cyejing.dam.core.container;

import cn.cyejing.dam.core.config.Config;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.io.IOException;


@Slf4j
public class NettyContainer {

    private NettyHttpServer nettyHttpServer;
    private AsyncHttpClient asyncHttpClient;

    private EventLoopGroup eventLoopGroupBoss;
    private EventLoopGroup eventLoopGroupWork;

    private final Config config;

    public NettyContainer(Config config) {
        this.config = config;
        init();
    }

    public void init() {
        initEventLoopGroup();

        initAsyncHttpClient();

        nettyHttpServer = new NettyHttpServer(config, eventLoopGroupBoss, eventLoopGroupWork, useEPoll());

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Shutdown-Container"));
    }

    private void initAsyncHttpClient() {
        DefaultAsyncHttpClientConfig.Builder clientBuilder = new DefaultAsyncHttpClientConfig.Builder()
                .setFollowRedirect(false)
                .setEventLoopGroup(eventLoopGroupWork)
                .setConnectTimeout(config.getHttpConnectTimeout())
                .setRequestTimeout(config.getHttpRequestTimeout())
                .setReadTimeout(config.getHttpReadTimeout())
                .setMaxRequestRetry(config.getHttpMaxRequestRetry())
                .setAllocator(PooledByteBufAllocator.DEFAULT)
                .setCompressionEnforced(true)
                .setMaxConnections(config.getHttpMaxConnections())
                .setMaxConnectionsPerHost(config.getHttpMaxConnectionsPerHost())
                .setPooledConnectionIdleTimeout(config.getHttpPooledConnectionIdleTimeout());

        asyncHttpClient = new DefaultAsyncHttpClient(clientBuilder.build());
        NettyClient.setClient(asyncHttpClient);
    }

    private void initEventLoopGroup() {
        if (useEPoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(config.getEventLoopGroupBossNum(), new DefaultThreadFactory("NettyBossEPOLL"));
            this.eventLoopGroupWork = new EpollEventLoopGroup(config.getEventLoopGroupWorkNum(), new DefaultThreadFactory("NettyWorkEPOLL"));
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(config.getEventLoopGroupBossNum(), new DefaultThreadFactory("NettyBoss"));
            this.eventLoopGroupWork = new NioEventLoopGroup(config.getEventLoopGroupWorkNum(), new DefaultThreadFactory("NettyWork"));
        }
    }


    public boolean useEPoll() {
        return config.isUseEPoll() && Epoll.isAvailable() && System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("linux");
    }


    public void start() {
        nettyHttpServer.start(config.getPort());
    }

    public void shutdown() {
        try {
            eventLoopGroupBoss.shutdownGracefully();
            eventLoopGroupWork.shutdownGracefully();
            asyncHttpClient.close();
        } catch (IOException e) {
            log.error("Shutdown Container error", e);
        }
    }

}
