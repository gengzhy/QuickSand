package xin.cosmos.basic.define;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "单个请求参数")
@Data
public class SingleParam<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "单个请求参数，参数类型由传入的参数类型确定", required = true)
    private T key;
}
