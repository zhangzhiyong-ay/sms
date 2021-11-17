package net.henanyuanhang.sms.htip.example;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.common.message.CommonCodeMessage;
import net.henanyuanhang.sms.htip.message.HtipCodeMessageFactory;

import java.util.HashMap;
import java.util.Map;

import static net.henanyuanhang.sms.common.message.CommonCodeMessage.COMMON_SERVICE_ERROR;
import static net.henanyuanhang.sms.common.message.CommonCodeMessage.COMMON_TEMPLATE_ID_ILLEGAL;

/**
 * 自定义扩展codeMessage
 *
 * @author zhangzhiyong
 * @createTime 2021年11月17日 22:28
 */
public class CustomCodeMessageExample {

    public static void main(String[] args) {
        System.out.println("----------扩展之前的message值-----------------");
        CodeMessage codeMessage = new HtipCodeMessageFactory().create();
        System.out.println("code:0000000 = " + codeMessage.getMessage("0000000"));
        System.out.println("code:0000001 = " + codeMessage.getMessage("0000001"));
        System.out.println("code:0000002 = " + codeMessage.getMessage("0000002"));
        System.out.println("code:" + COMMON_TEMPLATE_ID_ILLEGAL + " = " + codeMessage.getMessage(COMMON_TEMPLATE_ID_ILLEGAL));
        System.out.println("code:" + COMMON_SERVICE_ERROR + " = " + codeMessage.getMessage(COMMON_SERVICE_ERROR));

        System.out.println("----------扩展HtipCodeMessage的message值-----------------");

        // 自定义扩展 河南电信综合短信业务平台codeMessage
        Map<String, String> customMessage = new HashMap<>();
        customMessage.put("0000000", "success");
        customMessage.put("0000002", "common error");


        codeMessage = new HtipCodeMessageFactory(customMessage).create();
        System.out.println("code:0000000 = " + codeMessage.getMessage("0000000"));
        System.out.println("code:0000001 = " + codeMessage.getMessage("0000001"));
        System.out.println("code:0000002 = " + codeMessage.getMessage("0000002"));
        System.out.println("code:" + COMMON_TEMPLATE_ID_ILLEGAL + " = " + codeMessage.getMessage(COMMON_TEMPLATE_ID_ILLEGAL));
        System.out.println("code:" + COMMON_SERVICE_ERROR + " = " + codeMessage.getMessage(COMMON_SERVICE_ERROR));

        System.out.println("----------扩展CommonCodeMessage的message值-----------------");
        // 自定义扩展通用的codeMessage
        CommonCodeMessage instance = CommonCodeMessage.getInstance();
        Map<String, String> commonMessage = new HashMap<>();
        commonMessage.put("COMMON_TEMPLATE_ID_ILLEGAL", "模板ID错误");
        instance.putCodeMessage(commonMessage);
        System.out.println("code:" + COMMON_TEMPLATE_ID_ILLEGAL + " = " + codeMessage.getMessage(COMMON_TEMPLATE_ID_ILLEGAL));
        System.out.println("code:" + COMMON_SERVICE_ERROR + " = " + codeMessage.getMessage(COMMON_SERVICE_ERROR));
    }
}
