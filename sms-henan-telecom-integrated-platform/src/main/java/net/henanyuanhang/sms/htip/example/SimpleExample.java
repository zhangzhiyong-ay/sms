package net.henanyuanhang.sms.htip.example;

import net.henanyuanhang.sms.core.sender.SmsExecuteTemplate;
import net.henanyuanhang.sms.core.sender.SmsExecuteTemplateBuilder;
import net.henanyuanhang.sms.core.sender.result.SendResult;
import net.henanyuanhang.sms.core.sender.result.SendResultCallback;
import net.henanyuanhang.sms.core.sender.result.SendResultData;
import net.henanyuanhang.sms.htip.profile.HtipProfile;
import net.henanyuanhang.sms.htip.sender.HtipSmsExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 发送单个短信
 */
public class SimpleExample {

    // appId、appSecret1、extendedCode可在平台我的信息中查看
    String appId = "";
    String appSecret1 = "";
    String extendedCode = ""; // 扩展码
    // sendUrl可在平台模板短信接口规范中查看
    String sendUrl = "";

    String phoneNumber = "";
    String templateId = "";

    public static void main(String[] args) {
        SimpleExample example = new SimpleExample();
        example.sync();
        example.async();
    }

    /**
     * 异步发送
     */
    public void async() {

        // 接收短信的手机号
        String phoneNumber = this.phoneNumber;
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
        HtipSmsExecutor htipSmsExecutor = new HtipSmsExecutor(htipProfile);

        // 创建短信发送模板。通常在项目中创建一个全局对象即可使用
        SmsExecuteTemplate smsExecuteTemplate = SmsExecuteTemplateBuilder.create()
                .smsExecutor(htipSmsExecutor)
                .build();

        // 发送单个短信
        smsExecuteTemplate.asyncSend(phoneNumber, templateId, templateParam, new SendResultCallback() {
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
     * 同步发送
     */
    public void sync() {

        // 接收短信的手机号
        String phoneNumber = this.phoneNumber;
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
        HtipSmsExecutor htipSmsExecutor = new HtipSmsExecutor(htipProfile);

        // 创建短信发送模板。通常在项目中创建一个全局对象即可使用
        SmsExecuteTemplate smsExecuteTemplate = SmsExecuteTemplateBuilder.create()
                .smsExecutor(htipSmsExecutor)
                .build();

        // 发送单个短信
        SendResult sendResult = smsExecuteTemplate.send(phoneNumber, templateId, templateParam);


        // 使用回调函数的方式处理短信发送结果
        sendResult.resultCallback(new SendResultCallback() {
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

        // 通过调用get方法分别获取发送成功和失败的数据进行处理
//        List<SendResultData> successData = sendResult.getSuccessData();
//        for (SendResultData successDatum : successData) {
//            System.out.println("短信发送结果：" + successDatum);
//        }
//
//        List<SendResultData> failData = sendResult.getFailData();
//
//        for (SendResultData failDatum : failData) {
//            System.out.println("短信发送结果：" + failData);
//        }

        smsExecuteTemplate.shutdown();

    }
}
