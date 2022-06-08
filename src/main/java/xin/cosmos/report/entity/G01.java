package xin.cosmos.report.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "G01资产负债项目统计表-实体")
@Data
public class G01 {
    //todo
    @ExcelProperty(value = "序号")
    private int index;
}
