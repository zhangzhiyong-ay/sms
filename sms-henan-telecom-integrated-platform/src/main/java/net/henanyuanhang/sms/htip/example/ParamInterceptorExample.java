package net.henanyuanhang.sms.htip.example;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.core.interceptor.ParamInterceptor;
import net.henanyuanhang.sms.core.interceptor.ParamInterceptorManager;
import net.henanyuanhang.sms.core.sender.SmsExecuteTemplate;
import net.henanyuanhang.sms.core.sender.SmsExecuteTemplateBuilder;
import net.henanyuanhang.sms.core.sender.SmsExecutor;
import net.henanyuanhang.sms.core.sender.result.SendResultCallback;
import net.henanyuanhang.sms.core.sender.result.SendResultData;
import net.henanyuanhang.sms.htip.example.interceptor.PhoneNumberRegexInterceptor;
import net.henanyuanhang.sms.htip.message.HtipCodeMessageFactory;
import net.henanyuanhang.sms.htip.profile.HtipProfile;
import net.henanyuanhang.sms.htip.sender.HtipSmsExecutor;

import java.util.*;

/**
 * 自定义增加短信发送参数拦截器
 *
 * @author zhangzhiyong
 * @createTime 2021年11月17日 21:58
 */
public class ParamInterceptorExample {

    // appId、appSecret1、extendedCode可在平台我的信息中查看
    String appId = "";
    String appSecret1 = "";
    String extendedCode = ""; // 扩展码
    // sendUrl可在平台模板短信接口规范中查看
    String sendUrl = "";

    String phoneNumber1 = "";
    String phoneNumber2 = "";
    String templateId = "";

    public static void main(String[] args) {
        Map<String, String> codeMessageMapping = new HashMap<>();
        codeMessageMapping.put(PhoneNumberRegexInterceptor.ERROR_CODE, "手机号不符合正则表达式要求");
        CodeMessage codeMessage = new HtipCodeMessageFactory(codeMessageMapping).create();

        ParamInterceptorExample example = new ParamInterceptorExample();
        example.async(codeMessage);

        ParamInterceptorExample example2 = new ParamInterceptorExample();
        example2.interceptorSort();
    }

    /**
     * 异步发送
     */
    public void async(CodeMessage codeMessage) {

        // 接收短信的手机号
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(phoneNumber1);
        phoneNumbers.add(phoneNumber2);
        // 短信模板ID，在平台上新增模板后，会自动生成ID
        String templateId = this.templateId;
        // 短信模板参数，根据配置的短信模板中的参数进行配置
        Map<String, String> templateParam = new HashMap<>();

        StringBuilder codeBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            codeBuilder.append(random.nextInt(9));
        }
        templateParam.put("code", codeBuilder.toString());

        // 创建配置文件
        HtipProfile htipProfile = new HtipProfile();
        htipProfile.setSendUrl(sendUrl);
        htipProfile.setAppId(appId);
        htipProfile.setAppSecret1(appSecret1);
        htipProfile.setExtendedCode(extendedCode);

        // 创建短信发送执行器
        SmsExecutor smsExecutor = new HtipSmsExecutor(htipProfile);

        // 创建拦截器管理器，将拦截器添加到拦截器链末尾
        ParamInterceptorManager paramInterceptorManager = new ParamInterceptorManager();
        paramInterceptorManager.addLast(new PhoneNumberRegexInterceptor(codeMessage));

        // 创建短信发送模板。通常在项目中创建一个全局对象即可使用
        SmsExecuteTemplate smsExecuteTemplate = SmsExecuteTemplateBuilder.create()
                .smsExecutor(smsExecutor)
                .paramInterceptorManager(paramInterceptorManager)
                .build();

        // 发送单个短信
        smsExecuteTemplate.asyncSend(phoneNumbers, templateId, templateParam, new SendResultCallback() {
            @Override
            public void onSuccess(List<SendResultData> successData) {
                for (SendResultData successDatum : successData) {
                    System.out.println("短信发送结果：" + successDatum);
                }
            }

            @Override
            public void onFail(List<SendResultData> failData) {
                for (SendResultData failDatum : failData) {
                    System.out.println("短信发送结果：" + failData);
                }
            }
        });
        System.out.println("等待异步发送结果...");
        smsExecuteTemplate.shutdown();
    }

    /**
     * 自定义拦截器
     */
    public void interceptorSort() {
        // 创建 ParamInterceptorManager 时，默认在拦截器链开头增加一个拦截器：NullParamsInterceptor，此时排序：NullParamsInterceptor
        ParamInterceptorManager paramInterceptorManager = new ParamInterceptorManager();

        paramInterceptorManager.addLast(new InterceptorC()); // 排序：NullParamsInterceptor > InterceptorC
        paramInterceptorManager.addBefore(new InterceptorA(), InterceptorC.class);// 排序：NullParamsInterceptor >InterceptorA > InterceptorC
        paramInterceptorManager.addAfter(new InterceptorB(), InterceptorA.class);// 排序：NullParamsInterceptor >InterceptorA >InterceptorB > InterceptorC

        List<ParamInterceptor> interceptors = paramInterceptorManager.getInterceptors();
        for (ParamInterceptor interceptor : interceptors) {
            System.out.println(interceptor.getClass().getName());
        }
        System.out.println("------------------------");

        paramInterceptorManager.remove(InterceptorA.class); // 排序：NullParamsInterceptor > InterceptorB > InterceptorC
        paramInterceptorManager.addFirst(new InterceptorA()); // 排序：InterceptorA > NullParamsInterceptor >InterceptorB > InterceptorC


        interceptors = paramInterceptorManager.getInterceptors();
        for (ParamInterceptor interceptor : interceptors) {
            System.out.println(interceptor.getClass().getName());
        }
        System.out.println("------------------------");

        paramInterceptorManager.remove(InterceptorB.class); // 排序：InterceptorA > NullParamsInterceptor > InterceptorC
        paramInterceptorManager.addAt(new InterceptorB(), InterceptorA.class); // 排序：InterceptorB > NullParamsInterceptor > InterceptorC


        interceptors = paramInterceptorManager.getInterceptors();
        for (ParamInterceptor interceptor : interceptors) {
            System.out.println(interceptor.getClass().getName());
        }
        System.out.println("------------------------");
    }

    class InterceptorA implements ParamInterceptor {

        @Override
        public SendResultData intercept(String phoneNumber, String templateId, Map<String, String> templateParam) {
            return null;
        }
    }

    class InterceptorB implements ParamInterceptor {

        @Override
        public SendResultData intercept(String phoneNumber, String templateId, Map<String, String> templateParam) {
            return null;
        }
    }

    class InterceptorC implements ParamInterceptor {

        @Override
        public SendResultData intercept(String phoneNumber, String templateId, Map<String, String> templateParam) {
            return null;
        }
    }

}
