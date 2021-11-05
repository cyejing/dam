package cn.cyejing.dam.example;

import cn.cyejing.dam.core.config.ConfigLoader;
import cn.cyejing.dam.core.container.DamContainer;

public class DamBootstrap {

    public static void main(String[] args) {
        new DamContainer(ConfigLoader.load(args)).start();
    }

}
