package net.henanyuanhang.sms.core.sender;

import net.henanyuanhang.sms.core.sender.result.SendResult;

public interface SendFunction {

    SendResult send(Executor smsSender);
}
