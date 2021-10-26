package net.henanyuanhang.core.sender.result;

/**
 * 短信发送状态
 */
public interface SmsSendStatus {

    /**
     * 结果唯一表示
     *
     * @return
     */
    String getId();

    /**
     * 结果编码
     *
     * @return
     */
    String getCode();

    /**
     * 结果文本描述
     *
     * @return
     */
    String getMessage();

    /**
     * 是否发送成功
     * @return
     */
    boolean success();
}
