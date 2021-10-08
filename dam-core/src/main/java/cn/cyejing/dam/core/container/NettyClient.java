package cn.cyejing.dam.core.container;

import org.asynchttpclient.AsyncHttpClient;

public abstract class NettyClient {

    private static AsyncHttpClient instance;

    public static AsyncHttpClient getClient() {
        if (instance == null) {
            throw new RuntimeException("AsyncHttpClient not initialized yet");
        }
        return instance;
    }

    static void setClient(AsyncHttpClient client) {
        instance = client;
    }
}
