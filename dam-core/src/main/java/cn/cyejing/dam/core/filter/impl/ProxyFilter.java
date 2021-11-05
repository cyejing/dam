package cn.cyejing.dam.core.filter.impl;

import cn.cyejing.dam.common.config.DefaultDynamicConfig;
import cn.cyejing.dam.common.config.Instance;
import cn.cyejing.dam.core.container.DamContainer;
import cn.cyejing.dam.core.context.DefaultResponse;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.context.RequestMutable;
import cn.cyejing.dam.core.filter.Filter;
import cn.cyejing.dam.core.filter.FilterChain;
import cn.cyejing.dam.core.filter.loadbalance.LoadBalanceFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ProxyFilter implements Filter<ProxyFilter.Config> {

    @Override
    public String getName() {
        return "proxy";
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public void filter(FilterChain chain, Exchange exchange, Config config) throws URISyntaxException {
        URI uri = new URI(config.getUri());
        RequestMutable requestMutable = exchange.getRequestMutable();

        if (LoadBalanceFactory.getLoadBalance(uri.getScheme()) != null) {
            String group = uri.getHost();
            Set<Instance> instances = DefaultDynamicConfig.getInstance().getInstances(group);
            Instance instance = LoadBalanceFactory.getLoadBalance(uri.getScheme()).select(exchange, instances);
            requestMutable.setUri(instance.getUri());
        } else {
            requestMutable.setUri(uri.toString());
        }

        CompletableFuture<Response> future = DamContainer.getClient().executeRequest(requestMutable.build()).toCompletableFuture();
        future.whenComplete((response, throwable) -> {
            exchange.releaseRequest();
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
    }

}
