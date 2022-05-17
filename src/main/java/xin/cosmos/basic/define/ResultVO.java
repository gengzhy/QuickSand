package xin.cosmos.basic.define;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;
import xin.cosmos.basic.constant.ResultCode;

@ToString
@Getter
@ApiModel(description = "公共返回类")
public class ResultVO<T> {

    @ApiModelProperty(value = "响应编码（响应成功返回success，非success均为失败）")
    private final String code;

    @ApiModelProperty(value = "响应消息")
    private final String message;

    @ApiModelProperty(value = "响应数据")
    private final T data;

    public ResultVO(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultVO(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public static <T> ResultVO<T> success(T data) {
        return success(ResultCode.SUCCESS.getDesc(), data);
    }

    public static <T> ResultVO<T> success(String message, T data) {
        return new ResultVO<>(ResultCode.SUCCESS.name().toLowerCase(), message, data);
    }

    public static <T> ResultVO<T> failed() {
        return new ResultVO<>(ResultCode.FAILED.name().toLowerCase(), ResultCode.FAILED.getDesc());
    }

    public static <T> ResultVO<T> failed(String message) {
        return new ResultVO<>(ResultCode.FAILED.name().toLowerCase(), message);
    }

    public static <T> ResultVO<T> failed(ResultCode failedCode) {
        return new ResultVO<>(failedCode.name().toLowerCase(), failedCode.getDesc());
    }

    public static <T> ResultVO<T> failed(ResultCode failedCode, String message) {
        return new ResultVO<>(failedCode.name().toLowerCase(), message);
    }

    public static <T> ResultVO<T> failed(String code, String message) {
        return new ResultVO<>(code, message);
    }
}
