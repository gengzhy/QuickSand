package xin.cosmos.basic.ocr.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(description = "营业执照对象")
@Data
public class BusinessLicense {

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "单位类型")
    private String unitType;

    @ApiModelProperty(value = "公司法人")
    private String legalPerson;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "有效期")
    private String validity;

    @ApiModelProperty(value = "证件编号")
    private String idNumber;

    @ApiModelProperty(value = "社会信用代码")
    private String code;

    @ApiModelProperty(value = "注册资本")
    private String registeredCapital;

    @ApiModelProperty(value = "成立日期")
    private String companyCreateDate;

    @ApiModelProperty(value = "组成形式")
    private String layout;

    @ApiModelProperty(value = "经营范围")
    private String businessScope;

}
