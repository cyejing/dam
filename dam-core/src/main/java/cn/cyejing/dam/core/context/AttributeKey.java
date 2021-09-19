package cn.cyejing.dam.core.context;

import cn.cyejing.dam.common.enums.EnumLoadBalance;
import cn.cyejing.dam.common.module.Instance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public abstract class AttributeKey<T> {

    private static final Map<String, AttributeKey<?>> nameMap = new HashMap<>();

    public static final AttributeKey<String> CONSISTENT_HASH_KEY = create(String.class);
    public static final AttributeKey<EnumLoadBalance> LOAD_BALANCE_ENUM_KEY = create(EnumLoadBalance.class);
    public static final AttributeKey<Long> AUTH_ID_KEY = create(Long.class);
    public static final AttributeKey<String> CLIENT_ID_KEY = create(String.class);
    public static final AttributeKey<String> SERVICE_NAME_KEY = create(String.class);
    public static final AttributeKey<Map<String, String>> DUBBO_ATTACHMENT = create(Map.class);
    public static final AttributeKey<Set<Instance>> MATCH_INSTANCES = create(Set.class);
    public static final AttributeKey<Set<String>> MATCH_ADDRESS = create(Set.class);
    public static final AttributeKey<Instance> LOAD_INSTANCE = create(Instance.class);
    public static final AttributeKey<String> MATCH_TAGS = create(String.class);

    static {
        nameMap.put("authId", AUTH_ID_KEY);
        nameMap.put("clientId", CLIENT_ID_KEY);
        nameMap.put("serviceName", SERVICE_NAME_KEY);
    }


    
    public static AttributeKey<?> valueOf(String name) {
        return nameMap.get(name);
    }

    
    public abstract T cast(Object value);

    
	public static <T> AttributeKey<T> create(final Class<? super T> valueClass) {
        return new SimpleAttributeKey(valueClass);
    }

    
    public static class SimpleAttributeKey<T> extends AttributeKey<T> {
        private final Class<T> valueClass;

        SimpleAttributeKey(final Class<T> valueClass) {
            this.valueClass = valueClass;
        }

        public T cast(final Object value) {
            return valueClass.cast(value);
        }

        @Override
        public String toString() {
            if (valueClass != null) {
                StringBuilder sb = new StringBuilder(getClass().getName());
                sb.append("<");
                sb.append(valueClass.getName());
                sb.append(">");
                return sb.toString();
            }
            return super.toString();
        }
    }
}
