package cn.cyejing.dam.core.filter.impl;

import cn.cyejing.dam.common.enums.EnumLoadBalance;
import cn.cyejing.dam.common.module.DefaultDynamicConfig;
import cn.cyejing.dam.common.module.Instance;
import cn.cyejing.dam.core.container.NettyClient;
import cn.cyejing.dam.core.context.DefaultResponse;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.context.RequestMutable;
import cn.cyejing.dam.core.filter.Filter;
import cn.cyejing.dam.core.filter.FilterChain;
import cn.cyejing.dam.core.filter.loadbalance.LoadBalanceFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ProxyFilter implements Filter<ProxyFilter.Config> {

    AsyncHttpClient client = NettyClient.getClient();


    @Override
    public String getName() {
        return "proxy";
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 1000;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public void filter(FilterChain chain, Exchange exchange, Config config) throws URISyntaxException {
        URI uri = new URI(config.getUri());
        RequestMutable requestMutable = exchange.getRequestMutable();
        if ("lb".equals(uri.getScheme())) {
            String host = uri.getHost();
            Set<Instance> instances = DefaultDynamicConfig.getInstance().getInstances(host);
            Instance instance = LoadBalanceFactory.getLoadBalance(config.getLoadBalance()).select(exchange, instances);

            requestMutable.setAddress(instance.getAddress());
        } else {
            requestMutable.setAddress(uri.getHost() + ":" + uri.getPort());
        }

        CompletableFuture<Response> future = client.executeRequest(requestMutable.build()).toCompletableFuture();
        future.whenComplete((response, throwable) -> {
            if (throwable != null) {
                exchange.occurError(throwable);
            } else {
                exchange.completedAndResponse(new DefaultResponse(response));
            }
            chain.doFilter();
        });

    }

    @Data
    public static class Config {
        private String uri;
        private EnumLoadBalance loadBalance = EnumLoadBalance.RANDOM;
    }

}
