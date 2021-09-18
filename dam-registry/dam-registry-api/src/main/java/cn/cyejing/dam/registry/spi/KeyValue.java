package cn.cyejing.dam.registry.spi;

import lombok.Data;


@Data
public class KeyValue {
    private String version;
    private String key;
    private String value;
    private Long ttl;

    public KeyValue() {
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(String key, String value, Long ttl) {
        this.key = key;
        this.value = value;
        this.ttl = ttl;
    }
}
