package net.henanyuanhang.sms.ctip.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * MD5摘要工具类
 */
public class MD5Utils {

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 获取md5计算出的32位小写结果
     *
     * @param data
     * @param charset
     * @return
     */
    public static String md5Hex(String data, Charset charset) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(data.getBytes(charset));
            final int len = digest.length;
            final char[] out = new char[len << 1];//len*2
            // 小写
            for (int i = 0, j = 0; i < len; i++) {
                out[j++] = DIGITS_LOWER[(0xF0 & digest[i]) >>> 4];// 高位
                out[j++] = DIGITS_LOWER[0x0F & digest[i]];// 低位
            }
            return new String(out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
