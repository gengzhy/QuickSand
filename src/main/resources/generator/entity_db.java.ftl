package ${packageName};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

<#if entityDesc??>
@ApiModel(description = "${entityDesc}")
<#else>
@ApiModel(description = "${entityName}")
</#if>
@Entity
<#if tableName??>
@Table(name = "${tablePrefix!''}${tableName}")
<#else>
<#-- ${entityName?replace('([a-z])([A-Z]+)','$1_$2','r')?lower_case：表示将Java驼峰命名转换为全小写，下划线分隔 -->
@Table(name = "${tablePrefix!''}${entityName?replace('([a-z])([A-Z]+)','$1_$2','r')?lower_case}")
</#if>
@Getter
@Setter
@ToString
public class ${entityName} {
<#if props??>
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<#list props as prop>
<#if prop.type='STRING'>
    @ApiModelProperty(value = "${prop.remark}")
    @Column(name = "${prop.name?uncap_first}")
    private String ${prop.name?uncap_first};

<#elseif prop.type='DATE'>
    @ApiModelProperty(value = "${prop.remark}")
    @Column(name = "${prop.name?uncap_first}")
    private String ${prop.name?uncap_first};

<#elseif prop.type='NUMBER'>
    @ApiModelProperty(value = "${prop.remark}")
    @Column(name = "${prop.name?uncap_first}")
    private String ${prop.name?uncap_first};

<#elseif prop.type='PERCENT'>
    @ApiModelProperty(value = "${prop.remark}")
    @Column(name = "${prop.name?uncap_first}")
    private String ${prop.name?uncap_first};

<#elseif prop.type='BOOLEAN'>
    @ApiModelProperty(value = "${prop.remark}")
    @Column(name = "${prop.name?uncap_first}")
    private boolean ${prop.name?uncap_first};
</#if>
</#list>
</#if>
}