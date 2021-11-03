package net.henanyuanhang.sms.common.utils;

import java.util.Arrays;
import java.util.Map;

/**
 * 字符串工具类
 */
public class StringUtils {


    public static final char C_BACKSLASH = '\\';
    public static final char C_BRACE_LEFT = '{';
    public static final char C_BRACE_RIGHT = '}';

    public static final String EMPTY_JSON = "{}";

    public static final String EMPTY = "";

    public static final int INDEX_NOT_FOUND = -1;


    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }


    public static boolean isNotEmpty(String reasonPhrase) {
        return !isEmpty(reasonPhrase);
    }

    public static int length(String value) {
        return value == null ? 0 : value.length();
    }

    public static int length(String... values) {
        if (ArrayUtils.isEmpty(values)) {
            return 0;
        }
        return Arrays.stream(values).mapToInt(String::length).sum();
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, String... params) {
        if (isEmpty(template) || ArrayUtils.isEmpty(params)) {
            return template;
        }

        int delimLength = EMPTY_JSON.length();

        final int templateLength = template.length();

        // 初始化定义好的长度以获得更好的性能
        StringBuilder sbuf = new StringBuilder(templateLength + length(params));

        int handledPosition = 0;// 记录已经处理到的位置
        int delimIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < params.length; argIndex++) {
            delimIndex = template.indexOf(EMPTY_JSON, handledPosition);
            if (delimIndex == -1) {// 剩余部分无占位符
                if (handledPosition == 0) { // 不带占位符的模板直接返回
                    return template;
                }
                // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                sbuf.append(template, handledPosition, templateLength);
                return sbuf.toString();
            }
            sbuf.append(template, handledPosition, delimIndex);
            sbuf.append(params[argIndex]);
            handledPosition = delimIndex + delimLength;

        }

        // 加入最后一个占位符后所有的字符
        sbuf.append(template, handledPosition, template.length());

        return sbuf.toString();
    }

    /**
     * 格式化文本，使用 {varName} 占位<br>
     * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param map      参数值对
     * @return 格式化后的文本
     */
    public static String format(String template, Map<String, String> map) {
        if (null == template) {
            return null;
        }
        if (null == map || map.isEmpty()) {
            return template;
        }

        String value;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            value = entry.getValue();
            if (null != value) {
                template = template.replace("{" + entry.getKey() + "}", value);
            }
        }
        return template;
    }

}
