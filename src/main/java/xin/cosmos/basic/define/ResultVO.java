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

    @ApiModelProperty(value = "响应编码（响应成功返回S，非S均为失败）")
    private final ResultCode code;

    @ApiModelProperty(value = "响应消息")
    private final String message;

    @ApiModelProperty(value = "响应数据")
    private final T data;

    public ResultVO(ResultCode code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultVO(ResultCode code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public static <T> ResultVO<T> success() {
        return success(null);
    }

    public static <T> ResultVO<T> success(String message) {
        return success(message, null);
    }

    public static <T> ResultVO<T> success(T data) {
        return success(ResultCode.S.getMessage(), data);
    }

    public static <T> ResultVO<T> success(String message, T data) {
        return new ResultVO<>(ResultCode.S, message, data);
    }

    public static <T> ResultVO<T> failed() {
        return new ResultVO<>(ResultCode.E, ResultCode.E.getMessage());
    }

    public static <T> ResultVO<T> failed(String message) {
        return new ResultVO<>(ResultCode.E, message);
    }

    public static <T> ResultVO<T> failed(ResultCode failedCode) {
        return new ResultVO<>(failedCode, failedCode.getMessage());
    }

    public static <T> ResultVO<T> failed(ResultCode failedCode, String message) {
        return new ResultVO<>(failedCode, message);
    }
}
