package cn.cyejing.dam.it.base;

import cn.cyejing.dam.core.config.Config;
import cn.cyejing.dam.core.config.ConfigLoader;
import cn.cyejing.dam.core.container.DamContainer;
import io.netty.buffer.PooledByteBufAllocator;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

public class BaseIT {
    private static final AsyncHttpClient ASYNC_HTTP_CLIENT;

    static {
        Config config = new Config();
        DefaultAsyncHttpClientConfig.Builder clientBuilder = new DefaultAsyncHttpClientConfig.Builder()
                .setFollowRedirect(false)
                .setConnectTimeout(config.getHttpConnectTimeout())
                .setRequestTimeout(config.getHttpRequestTimeout())
                .setMaxRequestRetry(config.getHttpMaxRequestRetry())
                .setAllocator(PooledByteBufAllocator.DEFAULT)
                .setCompressionEnforced(true)
                .setMaxConnections(config.getHttpMaxConnections())
                .setMaxConnectionsPerHost(config.getHttpMaxConnectionsPerHost())
                .setPooledConnectionIdleTimeout(config.getHttpPooledConnectionIdleTimeout());

        ASYNC_HTTP_CLIENT = new DefaultAsyncHttpClient(clientBuilder.build());
    }

    public static DamContainer startCore() {
        return new DamContainer(ConfigLoader.getInstance().load(null));
    }

    public static AsyncHttpClient startClient() {
        return ASYNC_HTTP_CLIENT;
    }
}
