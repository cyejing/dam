package cn.cyejing.dam.core.config;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Config {

    private int port = 8048;
    private String namespace = "dam";
    private String routePath = "route.yaml";
    private String registry;

    private int eventLoopGroupBossNum = 1;
    private int eventLoopGroupWorkNum = Runtime.getRuntime().availableProcessors();
    private boolean useEPoll = true;
    private boolean nettyAllocator = true;
    private int maxContentLength = 32 * 1024 * 1024;
    private int bufferSize = 1024 * 16;

    private int httpConnectTimeout = 5000;
    private int httpReadTimeout = -1;
    private int httpRequestTimeout = 20000;
    private int httpMaxRequestRetry = 2;
    private int httpMaxConnections = 10000;
    private int httpMaxConnectionsPerHost = 8000;
    private int httpPooledConnectionIdleTimeout = 60 * 1000;

}
