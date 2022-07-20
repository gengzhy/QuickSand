package xin.cosmos.disclosure.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "票据承兑人信息元数据")
@Data
public class MetaData {

    @ApiModelProperty("序号")
    @ExcelProperty(value = "序号")
    private Integer index;

    @ApiModelProperty("企业名称")
    @ExcelProperty(value = "企业名称")
    private String corpName;
}
