# sms
## 项目简介
一个可接入多端平台短信发送功能的开源项目SDK组件，在项目中可以通过maven的方式引入不同平台短信发送功能，
通过简单的配置文件即可快速接入短信发送组件，同时支持同步、异步、群发等短信发送方式，支持java项目、Springboot项目接入适用。
目前该项目处于开发阶段，计划支持的短信平台有：河南电信综合短信业务平台、阿里云、百度云、华为云、腾讯云、京东云、网易云、七牛云、又拍云等各大主流短信平。

## 模块说明
| 模块 | 说明|
| --- | --- |
| sms-common | 通用包|
| sms-core | 核心包|
| sms-http-extension | http组件扩展包|
| sms-henan-telecom-integrated-platform | 河南电信综合短信业务管理平台 | 

## 接入文档
### 河南电信综合短信业务管理平台

1、 申请账号

登录[河南电信综合短信业务管理平台](https://1.193.76.62:8180/login) ,在 **我的信息** 菜单中查看相应的参数

2、使用说明

2.1 基本使用

```java
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

public class SimpleExample {

    public static void main(String[] args) {
        // appId、appSecret1、extendedCode可在平台我的信息中查看
        String appId = "";
        String appSecret1 = "";
        String extendedCode = ""; // 扩展码

        // sendUrl可在平台模板短信接口规范中查看
        String sendUrl = "";

        // 接收短信的手机号
        String phoneNumber = "";
        // 短信模板ID，在平台上新增模板后，会自动生成ID
        String templateId = "";
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

    }
}
```

