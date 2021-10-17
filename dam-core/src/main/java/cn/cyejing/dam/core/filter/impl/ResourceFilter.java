package cn.cyejing.dam.core.filter.impl;

import cn.cyejing.dam.core.context.DefaultResponse;
import cn.cyejing.dam.core.context.Exchange;
import cn.cyejing.dam.core.exception.DamException;
import cn.cyejing.dam.core.exception.ErrorCode;
import cn.cyejing.dam.core.filter.Filter;
import cn.cyejing.dam.core.filter.FilterChain;
import lombok.Data;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

public class ResourceFilter implements Filter<ResourceFilter.Config> {

    @Override
    public String getName() {
        return "resource";
    }

    @Override
    public Class getConfigClass() {
        return Config.class;
    }

    @Override
    public void filter(FilterChain chain, Exchange exchange, Config config) throws Exception {
        String root = config.getRoot();
        String path = exchange.getRequestMutable().getPath();
        path = sanitizeUri(path);
        File file = new File(root + path);
        if (file.exists() && file.isFile()) {
            exchange.completedAndResponse(new DefaultResponse(file));
        } else {
            exchange.occurError(new DamException(ErrorCode.NOT_FOUND_RESOURCE));
        }
        chain.doFilter();
    }

    @Data
    public static class Config {
        private String root;
    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private static String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }

        if (uri.isEmpty() || uri.charAt(0) != '/') {
            throw new DamException(ErrorCode.URI_ILLEGALITY);
        }

        uri = uri.replace('/', File.separatorChar);

        if (uri.contains(File.separator + '.') ||
                uri.contains('.' + File.separator) ||
                uri.charAt(0) == '.' || uri.charAt(uri.length() - 1) == '.' ||
                INSECURE_URI.matcher(uri).matches()) {
            throw new DamException(ErrorCode.URI_ILLEGALITY);
        }

        return uri;
    }

}
