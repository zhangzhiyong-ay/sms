package net.henanyuanhang.sms.common.utils;

import java.util.Map;

/**
 * @author zhangzhiyong
 * @createTime 2021年11月17日 22:14
 */
public class MapUtils {

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean notEmpty(Map map) {
        return !isEmpty(map);
    }

    public static int size(Map map) {
        return isEmpty(map) ? 0 : map.size();
    }
}
