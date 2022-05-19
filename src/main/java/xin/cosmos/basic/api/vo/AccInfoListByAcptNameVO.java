package xin.cosmos.basic.api.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author geng
 */
@ApiModel(description = "根据票据承兑人名称查询票据承兑人信息列表 - 响应结果")
@Data
public class AccInfoListByAcptNameVO {
    
    @ApiModelProperty("响应码")
    @JSONField(name = "code")
    private int code;

    @ApiModelProperty("是否响应成功")
    @JSONField(name = "success")
    private boolean success;

    @ApiModelProperty("响应消息")
    @JSONField(name = "message")
    private String message;

    @ApiModelProperty("票据承兑人信息列表")
    @JSONField(name = "data")
    private List<Body> dataList;

    @ApiModel(description = "票据承兑人信息")
    @Data
    public static class Body {

        @ApiModelProperty("承兑人账号")
        @JSONField(name = "entAccount")
        private String billAcceptanceAccountCode;

        @ApiModelProperty("承兑人名称")
        @JSONField(name = "entName")
        private String billAcceptanceName;

        @ApiModelProperty("统一社会信用代码")
        @JSONField(name = "soccode")
        private String creditCode;

    }
}