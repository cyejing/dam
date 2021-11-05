package cn.cyejing.dam.core.filter.impl;

import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.filter.Filter;
import cn.cyejing.dam.core.filter.FilterChain;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class HeadersFilter implements Filter<HeadersFilter.Config> {
    @Override
    public String getName() {
        return "headers";
    }

    @Override
    public Class getConfigClass() {
        return Config.class;
    }

    @Override
    public void filter(FilterChain chain, Exchange exchange, Config config) throws Exception {
        if (CollectionUtils.isNotEmpty(config.getSetHeaders())) {
            for (String str : config.getSetHeaders()) {
                String[] kv = str.split(",");
                if (kv.length >= 2 && StringUtils.isNotBlank(kv[0]) && StringUtils.isNotBlank(kv[1])) {
                    exchange.getRequestMutable().setHeader(kv[0], kv[1]);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(config.getUnsetHeaders())) {
            for (String str : config.getUnsetHeaders()) {
                if (StringUtils.isNotBlank(str)) {
                    exchange.getRequestMutable().unsetHeader(str);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(config.getReplaceHeaders())) {
            for (String str : config.getReplaceHeaders()) {
                String[] kv = str.split(",");
                if (kv.length >= 3 && StringUtils.isNotBlank(kv[0]) && StringUtils.isNotBlank(kv[1])) {
                    String headerValue = exchange.getRequest().getHeaders().get(kv[0]);
                    exchange.getRequestMutable().setHeader(kv[0], headerValue.replaceAll(kv[1], kv[2]));
                }
            }
        }

        chain.doFilter();
    }

    @Data
    public static class Config {
        public List<String> setHeaders;
        public List<String> unsetHeaders;
        public List<String> replaceHeaders;
    }

}
