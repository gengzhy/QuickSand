package xin.cosmos.basic.ocr.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

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
    private Date birthDate;

    @ApiModelProperty(value = "住址")
    private String address;

    @ApiModelProperty(value = "公民身份号码")
    private String cardNumber;

    @ApiModelProperty(value = "签发机关")
    private String signOrg;

    @ApiModelProperty(value = "签发日期")
    private Date signDate;

    @ApiModelProperty(value = "失效日期")
    private Date expiredDate;

}
