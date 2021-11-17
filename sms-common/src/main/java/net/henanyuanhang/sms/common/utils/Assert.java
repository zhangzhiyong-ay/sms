package net.henanyuanhang.sms.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具
 */
public class Assert {


    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(CharSequence charSequence, String message) {
        if (StringUtils.isEmpty(charSequence)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }
}
