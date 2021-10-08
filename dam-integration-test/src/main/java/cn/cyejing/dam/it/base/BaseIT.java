package cn.cyejing.dam.it.base;

import cn.cyejing.dam.core.config.ConfigLoader;
import cn.cyejing.dam.core.container.DamContainer;
import cn.cyejing.dam.core.container.NettyClient;
import org.asynchttpclient.AsyncHttpClient;

public class BaseIT {

    public static DamContainer startCore() {
        return new DamContainer(ConfigLoader.getInstance().load(null));
    }

    public static AsyncHttpClient startClient() {
        return NettyClient.getClient();
    }
}
