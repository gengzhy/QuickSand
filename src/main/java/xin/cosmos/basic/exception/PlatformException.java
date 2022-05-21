package xin.cosmos.basic.exception;

import xin.cosmos.basic.constant.ResultCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 平台级别异常类
 * @author geng
 */
public class PlatformException extends RuntimeException {
    private static final Pattern FORMAT_PATTERN = Pattern.compile("%");
    private final ResultCode resultCode;

    public PlatformException(String message) {
        super(message);
        this.resultCode = ResultCode.E;
    }

    public PlatformException(ResultCode resultCode) {
        this(resultCode.getMessage(), resultCode);
    }

    public PlatformException(String message, ResultCode resultCode) {
        super(message);
        this.resultCode = resultCode;
    }

    /**
     * @param formatMessage 该参数可以是String.format("%s", "")格式的字符串
     * @param args          需格式化的参数
     */
    public PlatformException(String formatMessage, String... args) {
        this(ResultCode.E, formatMessage, args);
    }

    /**
     * @param resultCode    状态码
     * @param formatMessage 该参数可以是String.format("%s", "")格式的字符串
     * @param args          需格式化的参数
     */
    public PlatformException(ResultCode resultCode, String formatMessage, String... args) {
        // 避免格式化错误，进去掉特殊标识符“%”
        super(formatErrorMsg(formatMessage, args));
        this.resultCode = resultCode;
    }

    /**
     * 统计message参数中“%”出现的次数
     *
     * @param message
     * @return
     */
    private static String formatErrorMsg(String message, String[] args) {
        if (args == null || args.length == 0) {
            return message.replace("%s", "");
        }
        int count = 0;
        Matcher m = FORMAT_PATTERN.matcher(message);
        while (m.find()) {
            count++;
        }
        if (count != args.length) {
            return message.replace("%s", "");
        }
        return String.format(message, args);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
