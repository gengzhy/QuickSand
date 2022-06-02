package xin.cosmos.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xin.cosmos.basic.dict.bill.disclosure.BillAcceptanceMetaType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(description = "票据承兑人信息披露Excel下载请求参数")
@Data
public class BillAcceptanceExcelDownloadParam {

    @NotBlank
    @ApiModelProperty(value = "票据披露月份", example = "2022-04")
    private String showMonth;

    @NotNull
    @ApiModelProperty(value = "业务类型",example = "META_GUIZHOU_TOP100")
    private BillAcceptanceMetaType busiType;
}
