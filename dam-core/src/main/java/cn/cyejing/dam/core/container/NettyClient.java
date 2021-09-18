package cn.cyejing.dam.core.container;

import org.asynchttpclient.AsyncHttpClient;

public abstract class NettyClient {

    private static AsyncHttpClient INSTANCE;

    static void setClient(AsyncHttpClient client) {
        INSTANCE = client;
    }

    public static AsyncHttpClient getClient() {
        return INSTANCE;
    }
}
