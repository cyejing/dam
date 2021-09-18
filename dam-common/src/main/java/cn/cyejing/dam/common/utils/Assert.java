
package cn.cyejing.dam.common.utils;

import java.util.Collection;
import java.util.function.Supplier;


public class Assert {
    public Assert() {
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String str, String message) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }


    public static void notEmpty(Collection<?> coll, String message) {
        if (coll == null || coll.size() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    private static String nullSafeGet( Supplier<String> messageSupplier) {
        return messageSupplier != null ? (String)messageSupplier.get() : null;
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
}
