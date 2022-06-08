package xin.cosmos.disclosure.api.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "票据承兑信用信息披露记录")
@Data
public class FindSettlePageVO {

    @ApiModelProperty("响应码")
    @JSONField(name = "code")
    private int code;

    @ApiModelProperty("是否响应成功")
    @JSONField(name = "success")
    private boolean success;

    @ApiModelProperty("响应消息")
    @JSONField(name = "message")
    private String message;

    @ApiModelProperty("响应数据体")
    @JSONField(name = "data")
    private FindSettlePageVOBody data;

}
