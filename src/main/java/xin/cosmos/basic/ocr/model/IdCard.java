package xin.cosmos.basic.ocr.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "身份证对象")
@Data
public class IdCard {

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "出生日期")
    private String birthDate;

    @ApiModelProperty(value = "住址")
    private String address;

    @ApiModelProperty(value = "公民身份号码")
    private String cardNumber;

    @ApiModelProperty(value = "签发机关")
    private String signOrg;

    @ApiModelProperty(value = "签发日期")
    private String signDate;

    @ApiModelProperty(value = "失效日期")
    private String expiredDate;

}
