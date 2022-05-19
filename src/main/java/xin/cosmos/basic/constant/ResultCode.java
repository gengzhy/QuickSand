package xin.cosmos.basic.constant;

import lombok.Getter;

/**
 * 响应结果代码
 */
@Getter
public enum ResultCode {
    SUCCESS("响应成功", "操作成功"),
    FAILED("响应失败", "操作失败"),
    ;
    private String desc;
    private String message;

    ResultCode(String desc) {
        this.desc = desc;
        this.message = desc;
    }

    ResultCode(String desc, String message) {
        this.desc = desc;
        this.message = message;
    }
}
