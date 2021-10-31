package cn.cyejing.dam.core.container;

import cn.cyejing.dam.core.config.Config;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenyejing
 */
@Slf4j
public class DamContainer {
    private final Config config;

    @Getter
    private EventLoopGroup eventLoopGroupBoss;
    @Getter
    private EventLoopGroup eventLoopGroupWork;

    private List<DamServer> damServers = new ArrayList<>();

    private static AsyncHttpClient asyncHttpClient;



    public DamContainer(Config config) {
        this.config = config;

        initEventLoopGroup();

        initAsyncHttpClient();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Shutdown-Container"));
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
    }

    public void start() {
        this.damServers.add(new DamServer(config, this).start(config.getPort()));
    }

    public void start(int port) {
        this.damServers.add(new DamServer(config, this).start(port));
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

    public static AsyncHttpClient getClient() {
        if (asyncHttpClient == null) {
            throw new RuntimeException("AsyncHttpClient not initialized yet");
        }
        return asyncHttpClient;
    }
}
