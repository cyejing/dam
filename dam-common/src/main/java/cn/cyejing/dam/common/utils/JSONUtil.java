package cn.cyejing.dam.common.utils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.List;

public class JSONUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        MAPPER.addMixIn(Object.class, ExcludeFilter.class);
        MAPPER.setFilterProvider(new SimpleFilterProvider().addFilter("excludeFilter", SimpleBeanPropertyFilter.serializeAllExcept("class")));
    }

    public static String writeValueAsString(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("object format to json error:" + obj, e);
        }
    }

    public static JsonNode readValue(String str) {
        try {
            return MAPPER.readTree(str);
        } catch (Exception e) {
            throw new RuntimeException("json parse to object error", e);
        }
    }
    public static <T> T readValue(String str, Class<T> clz) {
        try {
            return MAPPER.readValue(str == null ? "{}" : str, clz);
        } catch (Exception e) {
            throw new RuntimeException("json parse to object [" + clz + "] error:" + str, e);
        }
    }

    public static <T> T readValue(String str, JavaType javaType) {
        try {
            return MAPPER.readValue(str, javaType);
        } catch (Exception e) {
            throw new RuntimeException("json parse to object [" + str + "] error:" + str, e);
        }
    }

    public static <T> T convertValue(Object obj, Class<T> clz) {
        return MAPPER.convertValue(obj, clz);
    }

    public static <T> List<T> readValueList(String json, Class<T> clz) {
        return readValue(json, getCollectionType(List.class, clz));
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    @JsonFilter("excludeFilter")
    public static class ExcludeFilter {

    }
}
