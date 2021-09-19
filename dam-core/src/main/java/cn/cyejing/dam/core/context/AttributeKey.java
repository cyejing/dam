package cn.cyejing.dam.core.context;

public abstract class AttributeKey<T> {

    public static final AttributeKey<String> SERVICE_NAME_KEY = create(String.class);

    public static <T> AttributeKey<T> create(final Class<? super T> valueClass) {
        return new SimpleAttributeKey(valueClass);
    }

    public abstract T cast(Object value);

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
