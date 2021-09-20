package cn.cyejing.dam.core.filter.impl;

import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.filter.Filter;
import cn.cyejing.dam.core.filter.FilterChain;
import lombok.Data;

public class RewriteFilter implements Filter<RewriteFilter.Config> {
    @Override
    public String getName() {
        return "rewrite";
    }

    @Override
    public int getOrder() {
        return 0;
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
    public void filter(FilterChain chain, Exchange exchange, Config config) throws Exception {
        String newPath = replacePath(exchange.getRequest().getPath(), config);
        exchange.getRequestMutable().setPath(newPath);
        chain.doFilter();
    }

    public String replacePath(String path, Config config) {
        String replacement = config.replacement.replace("$\\", "$");
        return path.replaceAll(config.regex, replacement);
    }

    @Data
    public static class Config {
        private String regex;
        private String replacement;
    }
}
