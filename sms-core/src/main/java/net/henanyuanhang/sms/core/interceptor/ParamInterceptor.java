package net.henanyuanhang.sms.core.interceptor;

import net.henanyuanhang.sms.core.sender.result.SendResultData;

import java.util.Map;

/**
 * 短信发送拦截器，拦截短信发送时的参数
 *
 * @author zhangzhiyong
 * @createTime 2021年11月11日
 */
public interface ParamInterceptor {

    /**
     * 拦截器，可以通过实现类来对短信发送时的参数进行拦截
     * <h1>注意：当返回结果为null时，拦截器才会继续向下调用，否则不会调用下一个拦截器，并将作为短信发送结果返回</h1>
     *
     * @param phoneNumber   手机号
     * @param templateId   短信模板key
     * @param templateParam 短信模板参数
     * @return 短信发送返回结果。如果返回值为null时，则将继续调用下一个拦截器，否则将作为最后的结果返回，且不调用下一个拦截器
     */
    SendResultData intercept(String phoneNumber, String templateId, Map<String, String> templateParam);

}
