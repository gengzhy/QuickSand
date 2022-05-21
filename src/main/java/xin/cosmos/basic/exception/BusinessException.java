package xin.cosmos.basic.exception;

import xin.cosmos.basic.constant.ResultCode;

/**
 * 业务级别的异常类
 * @author geng
 */
public class BusinessException extends PlatformException{

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode);
    }

    public BusinessException(String message, ResultCode resultCode) {
        super(message, resultCode);
    }

    /**
     * @param formatMessage 该参数可以是String.format("%s", "")格式的字符串
     * @param args          需格式化的参数
     */
    public BusinessException(String formatMessage, String... args) {
        super(formatMessage, args);
    }

    /**
     * @param resultCode    状态码
     * @param formatMessage 该参数可以是String.format("%s", "")格式的字符串
     * @param args          需格式化的参数
     */
    public BusinessException(ResultCode resultCode, String formatMessage, String... args) {
        super(resultCode, formatMessage, args);
    }
}
