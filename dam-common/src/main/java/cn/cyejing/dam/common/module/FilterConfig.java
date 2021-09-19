package cn.cyejing.dam.common.module;

import lombok.Data;

import java.util.Objects;


@Data
public class FilterConfig {

    private String name;
    private String param;
    private Boolean loggable = false;
    private Boolean opened = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterConfig that = (FilterConfig) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
