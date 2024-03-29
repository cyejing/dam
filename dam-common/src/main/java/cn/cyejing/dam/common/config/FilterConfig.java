package cn.cyejing.dam.common.config;

import lombok.Data;

import java.util.Objects;


@Data
public class FilterConfig {

    private String name;
    private Object params;
    private Boolean loggable = false;
    private Boolean opened = true;

    public String getName() {
        return name.toLowerCase();
    }

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
