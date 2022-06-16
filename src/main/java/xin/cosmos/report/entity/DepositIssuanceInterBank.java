package xin.cosmos.report.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ApiModel(description = "发行同业存单-页签")
@Entity
@Table(name = "fr_deposit_issuance_inter_bank")
@Getter
@Setter
@ToString
public class DepositIssuanceInterBank {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "序号")
    @ExcelProperty(value = "序号")
    @Column(name = "c_0")
    private String c_0;

    @ApiModelProperty(value = "发行日期")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(value = "发行日期")
    @Column(name = "c_1")
    private String c_1;

    @ApiModelProperty(value = "存单简称")
    @ExcelProperty(value = "存单简称")
    @Column(name = "c_2")
    private String c_2;

    @ApiModelProperty(value = "存单代码")
    @ExcelProperty(value = "存单代码")
    @Column(name = "c_3")
    private String c_3;

    @ApiModelProperty(value = "认购机构")
    @ExcelProperty(value = "认购机构")
    @Column(name = "c_4")
    private String c_4;

    @ApiModelProperty(value = "机构性质")
    @ExcelProperty(value = "机构性质")
    @Column(name = "c_5")
    private String c_5;

    @ApiModelProperty(value = "缴款日/起息日")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(value = "缴款日/起息日")
    @Column(name = "c_6")
    private String c_6;

    @ApiModelProperty(value = "发行价格（元）")
    @NumberFormat(value = "#.####")
    @ExcelProperty(value = "发行价格（元）")
    @Column(name = "c_7")
    private String c_7;

    @ApiModelProperty(value = "参考收益率")
    @NumberFormat(value = "#.######")
    @ExcelProperty(value = "参考收益率")
    @Column(name = "c_8")
    private String c_8;

    @ApiModelProperty(value = "期限")
    @ExcelProperty(value = "期限")
    @Column(name = "c_9")
    private String c_9;

    @ApiModelProperty(value = "到期日")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(value = "到期日")
    @Column(name = "c_10")
    private String c_10;

    @ApiModelProperty(value = "参考兑付日")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(value = "参考兑付日")
    @Column(name = "c_11")
    private String c_11;

    @ApiModelProperty(value = "认购量")
    @NumberFormat(value = "#.####")
    @ExcelProperty(value = "认购量")
    @Column(name = "c_12")
    private String c_12;

    @ApiModelProperty(value = "应付利息")
    @NumberFormat(value = "#.####")
    @ExcelProperty(value = "应付利息")
    @Column(name = "c_13")
    private String c_13;

    @ApiModelProperty(value = "缴款金额")
    @NumberFormat(value = "#.####")
    @ExcelProperty(value = "缴款金额")
    @Column(name = "c_14")
    private String c_14;

    @ApiModelProperty(value = "20191231账面金额")
    @NumberFormat(value = "#.####")
    @ExcelProperty(value = "20191231账面金额")
    @Column(name = "c_15")
    private String c_15;

    @ApiModelProperty(value = "实际投资人")
    @ExcelProperty(value = "实际投资人")
    @Column(name = "c_16")
    private String c_16;

}