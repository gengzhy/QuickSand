package xin.cosmos.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import xin.cosmos.basic.api.dict.bill.disclosure.ShowStatus;

@ApiModel(description = "票据承兑信用信息披露Excel下载-实体")
@Data
public class BillAcceptanceDisclosureDataExcelDownloadDTO {
    @ExcelProperty(value = "序号")
    private int index;
    //=============基本信息===============
    @ExcelProperty(value = "披露信息时点月份")
    private String showMonth;

    @ExcelProperty(value = "承兑人名称")
    private String acptName;

    @ExcelProperty(value = "机构类型")
    private String orgType;

    @ExcelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ExcelProperty(value = "注册日期")
    private String registerDate;

    //=============披露信息===============
    @ExcelProperty(value = "承兑人开户机构名称")
    private String dimAcptBranchName;

    @ExcelProperty(value = "累计承兑发生额（元）")
    private String acptAmount;
    @ExcelProperty(value = "承兑余额（元）")
    private String acptOver;

    @ExcelProperty(value = "累计逾期发生额（元）")
    private String totalOverdueAmount;
    @ExcelProperty(value = "逾期余额（元）")
    private String overdueOver;

    @ExcelProperty(value = "披露信息时点日期")
    private String showDate;
    @ExcelProperty(value = "披露日期")
    private String relDate;
    @ExcelProperty(value = "系统备注")
    private String remark;
    @ExcelProperty(value = "企业备注")
    private String entRemark;

    @ExcelIgnore
    @ExcelProperty(value = "entDetailRemark")
    private String entDetailRemark;

    @ExcelIgnore
    private ShowStatus showStatus;
}
