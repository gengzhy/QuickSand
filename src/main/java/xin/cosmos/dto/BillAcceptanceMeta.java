package xin.cosmos.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description = "票据承兑人信息Excel导入元数据-实体")
@ToString
@Data
public class BillAcceptanceMeta {

    @ApiModelProperty("序号")
    @ExcelProperty(value = "序号")
    private Integer index;

    @ApiModelProperty("企业名称")
    @ExcelProperty(value = "企业名称")
    private String corpName;
}
