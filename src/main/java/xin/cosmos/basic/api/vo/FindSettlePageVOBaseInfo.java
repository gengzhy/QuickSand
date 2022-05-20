package xin.cosmos.basic.api.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xin.cosmos.basic.api.dict.bill.disclosure.OrgType;
import xin.cosmos.basic.codec.ApiDictDeserializer;

@ApiModel(description = "票据承兑信用信息披露记录 - 披露主体基本信息")
@Data
public class FindSettlePageVOBaseInfo {
    @ApiModelProperty("承兑人名称")
    @JSONField(name = "entName")
    private String entName;

    @ApiModelProperty("统一社会信用代码")
    @JSONField(name = "soccode")
    private String creditCode;

    @ApiModelProperty(value = "机构类型", example = "1-企业")
    @JSONField(name = "acptOrgType", deserializeUsing = ApiDictDeserializer.class)
    private OrgType orgType;

    @ApiModelProperty("注册日期")
    @JSONField(name = "createTime")
    private String registerDate;

}