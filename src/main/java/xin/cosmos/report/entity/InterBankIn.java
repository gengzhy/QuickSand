package xin.cosmos.report.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(description = "InterBankIn")
//@Builder
@Data
public class InterBankIn {

    @ApiModelProperty(value = "序号")
    @ExcelProperty(value = "序号")
    private String c_0;

    @ApiModelProperty(value = "发行日期")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(value = "发行日期")
    private Date c_1;

    @ApiModelProperty(value = "存单简称")
    @ExcelProperty(value = "存单简称")
    private String c_2;

    @ApiModelProperty(value = "存单代码")
    @ExcelProperty(value = "存单代码")
    private String c_3;

    @ApiModelProperty(value = "认购机构")
    @ExcelProperty(value = "认购机构")
    private String c_4;

    @ApiModelProperty(value = "机构性质")
    @ExcelProperty(value = "机构性质")
    private String c_5;

    @ApiModelProperty(value = "缴款日/起息日")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(value = "缴款日/起息日")
    private Date c_6;

    @ApiModelProperty(value = "发行价格（元）")
    @ExcelProperty(value = "发行价格（元）")
    private BigDecimal c_7;

    @ApiModelProperty(value = "参考收益率")
    @ExcelProperty(value = "参考收益率")
    private BigDecimal c_8;

    @ApiModelProperty(value = "期限")
    @ExcelProperty(value = "期限")
    private String c_9;

    @ApiModelProperty(value = "到期日")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(value = "到期日")
    private Date c_10;

    @ApiModelProperty(value = "参考兑付日")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(value = "参考兑付日")
    private Date c_11;

    @ApiModelProperty(value = "认购量")
    @ExcelProperty(value = "认购量")
    private BigDecimal c_12;

    @ApiModelProperty(value = "应付利息")
    @ExcelProperty(value = "应付利息")
    private BigDecimal c_13;

    @ApiModelProperty(value = "缴款金额")
    @ExcelProperty(value = "缴款金额")
    private BigDecimal c_14;

    @ApiModelProperty(value = "20191231账面金额")
    @ExcelProperty(value = "20191231账面金额")
    private String c_15;

    @ApiModelProperty(value = "实际投资人")
    @ExcelProperty(value = "实际投资人")
    private String c_16;
}