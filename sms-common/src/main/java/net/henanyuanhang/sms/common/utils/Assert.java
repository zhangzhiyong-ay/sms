package net.henanyuanhang.sms.common.utils;

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
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
