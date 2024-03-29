package cn.cyejing.dam.common.config;

import lombok.Data;

import java.util.List;
import java.util.Objects;


@Data
public class Instance {

    private String group;

    private String uri;

    private Integer weight;

    private long registerTime;

    private List<String> tags;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return Objects.equals(group, instance.group) &&
                Objects.equals(uri, instance.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, uri);
    }


}
