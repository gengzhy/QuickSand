package ${packageName};

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

<#if entityDesc??>
@ApiModel(description = "${entityDesc}")
<#else>
@ApiModel(description = "${entityName}")
</#if>
@Builder
@Data
public class ${entityName} {
<#if props??>
<#list props as prop>
<#if prop.type='STRING'>

    @ApiModelProperty(value = "${prop.remark}")
    @ExcelProperty(value = "${prop.remark}")
    private String ${prop.name?uncap_first};
<#elseif prop.type='DATE'>

    @ApiModelProperty(value = "${prop.remark}")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(value = "${prop.remark}")
    private Date ${prop.name?uncap_first};
<#elseif prop.type='NUMBER'>

    @ApiModelProperty(value = "${prop.remark}")
    @ExcelProperty(value = "${prop.remark}")
    private BigDecimal ${prop.name?uncap_first};
<#elseif prop.type='BOOLEAN'>

    @ApiModelProperty(value = "${prop.remark}")
    @ExcelProperty(value = "${prop.remark}")
    private boolean ${prop.name?uncap_first};
</#if>
</#list>
</#if>
}