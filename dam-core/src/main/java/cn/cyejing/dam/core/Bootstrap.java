package cn.cyejing.dam.core;

import cn.cyejing.dam.core.config.ConfigLoader;
import cn.cyejing.dam.core.container.NettyContainer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Bootstrap {

    public static void main(String[] args) {
        new NettyContainer(ConfigLoader.getInstance().load(args)).start();
    }

}
