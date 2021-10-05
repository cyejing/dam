package cn.cyejing.dam.it.base;

import cn.cyejing.dam.core.config.Config;
import cn.cyejing.dam.core.container.DamContainer;
import cn.cyejing.dam.core.container.NettyClient;
import org.asynchttpclient.AsyncHttpClient;

public class BaseIT {

    public static DamContainer startCore() {
        return new DamContainer(new Config());
    }

    public static AsyncHttpClient startClient() {
        return NettyClient.getClient();
    }
}
