package xin.cosmos.basic.api.param;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "票据承兑信用信息披露查询 - 请求参数")
public class FindSettlePageParam {

    @NotBlank
    @ApiModelProperty(value = "票据承兑人名称")
    @JSONField(name = "acptName")
    private String billAcceptanceName;

    @NotBlank
    @ApiModelProperty(value = "承兑人账号")
    @JSONField(name = "entAccount")
    private String billAcceptanceAccountCode;

    @NotBlank
    @ApiModelProperty(value = "披露信息时点日期（月份）", example = "2022-04")
    @JSONField(name = "showMonth")
    private String showMonth;

    @ApiModelProperty(value = "分页 - 当前页", hidden = true)
    @JSONField(name = "current")
    private Integer current = 1;

    @ApiModelProperty(value = "分页 - 每页大小", hidden = true)
    @JSONField(name = "size")
    private Integer size = 100;

}
