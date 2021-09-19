package cn.cyejing.dam.common.module;

import lombok.Data;

import java.util.List;
import java.util.Objects;


@Data
public class Instance {

    private String host;

    private String address;

    private Integer weight;

    private long registerTime;

    private List<String> tags;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return Objects.equals(host, instance.host) &&
                Objects.equals(address, instance.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, address);
    }


}
