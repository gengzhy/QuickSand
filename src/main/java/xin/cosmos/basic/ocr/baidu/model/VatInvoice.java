package xin.cosmos.basic.ocr.baidu.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description = "增值税发票信息")
@Data
public class VatInvoice {
    @ApiModelProperty(value = "发票消费类型。不同消费类型输出：餐饮、电器设备、通讯、服务、日用品食品、医疗、交通、其他")
    private String ServiceType;
    @ApiModelProperty(value = "发票种类。不同类型发票输出：普通发票、专用发票、电子普通发票、电子专用发票、通行费电子普票、区块链发票、通用机打电子发票")
    private String invoiceType;
    @ApiModelProperty(value = "发票名称")
    private String invoiceTypeOrg;
    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;
    @ApiModelProperty(value = "发票号码")
    private String invoiceNum;
    @ApiModelProperty(value = "发票代码的辅助校验码，一般业务情景可忽略")
    private String invoiceCodeConfirm;
    @ApiModelProperty(value = "发票号码的辅助校验码，一般业务情景可忽略")
    private String invoiceNumConfirm;
    @ApiModelProperty(value = "增值税发票左上角标志。 包含：通行费,销项负数、代开、收购、成品油、其他")
    private String invoiceTag;
    @ApiModelProperty(value = "机打号码。仅增值税卷票含有此参数")
    private String machineNum;
    @ApiModelProperty(value = "机器编号。仅增值税卷票含有此参数")
    private String machineCode;
    @ApiModelProperty(value = "校验码。增值税专票无此参数")
    private String checkCode;
    @ApiModelProperty(value = "开票日期")
    private String invoiceDate;
    @ApiModelProperty(value = "购方名称")
    private String purchaserName;
    @ApiModelProperty(value = "购方纳税人识别号")
    private String purchaserRegisterNum;
    @ApiModelProperty(value = "购方地址及电话")
    private String purchaserAddress;
    @ApiModelProperty(value = "购方开户行及账号")
    private String purchaserBank;
    @ApiModelProperty(value = "密码区")
    private String password;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "联次信息。专票第一联到第三联分别输出：第一联：记账联、第二联：抵扣联、第三联：发票联；普通发票第一联到第二联分别输出：第一联：记账联、第二联：发票联")
    private String sheetNum;
    @ApiModelProperty(value = "是否代开")
    private String agent;
    @ApiModelProperty(value = "电子支付标识。仅区块链发票含有此参数")
    private String onlinePay;
    @ApiModelProperty(value = "销售方名称")
    private String sellerName;
    @ApiModelProperty(value = "销售方纳税人识别号")
    private String sellerRegisterNum;
    @ApiModelProperty(value = "销售方地址及电话")
    private String sellerAddress;
    @ApiModelProperty(value = "销售方开户行及账号")
    private String sellerBank;
    @ApiModelProperty(value = "合计金额")
    private String totalAmount;
    @ApiModelProperty(value = "合计税额")
    private String totalTax;
    @ApiModelProperty(value = "价税合计(大写)")
    private String amountInWords;
    @ApiModelProperty(value = "价税合计(小写)")
    private String amountInFiguers;
    @ApiModelProperty(value = "收款人")
    private String payee;
    @ApiModelProperty(value = "复核")
    private String checker;
    @ApiModelProperty(value = "开票人")
    private String noteDrawer;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "判断是否存在印章。返回“0或1”，1代表存在印章，0代表不存在印章，当 seal_tag=true 时返回该字段")
    private String company_seal;
    @ApiModelProperty(value = "印章识别结果内容。当 seal_tag=true 时返回该字段")
    private String seal_info;
    @ApiModelProperty(value = "发票明细")
    private List<DetailInfo> details;

    @ApiModel(description = "发票明细")
    @Data
    public static class DetailInfo {
        @ApiModelProperty(value = "货物名称")
        private String commodityName;
        @ApiModelProperty(value = "规格型号")
        private String commodityType;
        @ApiModelProperty(value = "单位")
        private String commodityUnit;
        @ApiModelProperty(value = "数量")
        private String commodityNum;
        @ApiModelProperty(value = "单价")
        private String commodityPrice;
        @ApiModelProperty(value = "金额")
        private String commodityAmount;
        @ApiModelProperty(value = "税率")
        private String commodityTaxRate;
        @ApiModelProperty(value = "税额")
        private String commodityTax;
        @ApiModelProperty(value = "车牌号。仅通行费增值税电子普通发票含有此参数")
        private String commodityPlateNum;
        @ApiModelProperty(value = "类型。仅通行费增值税电子普通发票含有此参数")
        private String commodityVehicleType;
        @ApiModelProperty(value = "通行日期起。仅通行费增值税电子普通发票含有此参数")
        private String commodityStartDate;
        @ApiModelProperty(value = "通行日期止。仅通行费增值税电子普通发票含有此参数")
        private String commodityEndDate;
    }
}
