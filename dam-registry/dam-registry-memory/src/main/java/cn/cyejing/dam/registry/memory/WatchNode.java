package cn.cyejing.dam.registry.memory;

import cn.cyejing.dam.registry.spi.Watch;
import lombok.Data;

/**
 * @author chenyejing
 */
@Data
public class WatchNode {
    private String key;
    private Watch watch;
    private boolean prefix;

    public WatchNode(String key, Watch watch, boolean prefix) {
        this.key = key;
        this.watch = watch;
        this.prefix = prefix;
    }
}
