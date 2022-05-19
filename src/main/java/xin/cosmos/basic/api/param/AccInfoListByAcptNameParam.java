package xin.cosmos.basic.api.param;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "根据票据承兑人名称查询票据承兑人信息列表 - 请求参数")
public class AccInfoListByAcptNameParam {

    @ApiModelProperty(value = "票据承兑人名称")
    @JSONField(name = "acptName")
    private String billAcceptanceName;

    @ApiModelProperty(value = "5位数字随机数")
    @JSONField(name = "random")
    private String random;

}
