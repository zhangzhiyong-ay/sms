package net.henanyuanhang.sms.core.interceptor;

import net.henanyuanhang.sms.common.message.CodeMessage;
import net.henanyuanhang.sms.common.message.CommonCodeMessage;
import net.henanyuanhang.sms.common.utils.StringUtils;
import net.henanyuanhang.sms.core.sender.result.SendResultData;

import java.util.Map;

/**
 * 短信发送参数为空拦截器
 *
 * @author zhangzhiyong
 * @createTime 2021年11月11日 21:14
 */
public class NullParamsInterceptor implements ParamInterceptor {

    private CodeMessage codeMessage;

    public NullParamsInterceptor() {
        this(CommonCodeMessage.getInstance());
    }

    public NullParamsInterceptor(CodeMessage codeMessage) {
        this.codeMessage = codeMessage;
    }

    @Override
    public SendResultData intercept(String phoneNumber, String templateId, Map<String, String> templateParam) {

        String errorCode = null;
        if (StringUtils.isEmpty(phoneNumber)) {
            errorCode = CommonCodeMessage.COMMON_PHONE_NUMBER_ILLEGAL;
        } else if (StringUtils.isEmpty(templateId)) {
            errorCode = CommonCodeMessage.COMMON_TEMPLATE_ID_ILLEGAL;
        }
        if (StringUtils.isNotEmpty(errorCode)) {
            return SendResultData.create()
                    .setSuccess(false)
                    .settemplateId(templateId)
                    .setPhoneNumber(phoneNumber)
                    .setTemplateParam(templateParam)
                    .setCode(errorCode)
                    .setMessage(codeMessage.getMessage(errorCode));
        }
        return null;
    }
}
