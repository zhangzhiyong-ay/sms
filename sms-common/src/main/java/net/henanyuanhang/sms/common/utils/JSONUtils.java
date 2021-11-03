package net.henanyuanhang.sms.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * json工具类
 */
public class JSONUtils {

    public static final Gson GSON = new GsonBuilder().create();

    public static String toString(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T toObject(String jsonValue, Class<T> tClass) {
        return GSON.fromJson(jsonValue, tClass);
    }
}
