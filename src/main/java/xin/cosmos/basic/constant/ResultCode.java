package xin.cosmos.basic.constant;

import lombok.Getter;

/**
 * 响应结果代码
 *
 * @author geng
 */
@Getter
public enum ResultCode {
    S("响应成功", "操作成功"),
    E("响应失败", "操作失败"),
    NO_SUCH_DICT("解析枚举失败", "不存在的枚举字典值"),
    ILLEGAL_PARAM("参数不合法", "参数不合法"),
    ILLEGAL_PROXY("代理配置不合法", "代理配置不合法"),

    ;

    /**
     * 描述
     */
    private String desc;

    /**
     * 响应消息
     */
    private String message;

    ResultCode(String desc, String message) {
        this.desc = desc;
        this.message = message;
    }
}
