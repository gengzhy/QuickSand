package xin.cosmos.basic.constant;

import lombok.Getter;

/**
 * 响应结果代码
 */
@Getter
public enum ResultCode {
    SUCCESS("响应成功"),
    FAILED("响应失败"),
    ;
    private String desc;

    ResultCode(String desc) {
        this.desc = desc;
    }
}
