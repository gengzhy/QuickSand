package xin.cosmos.basic.generator.model;

import lombok.Builder;
import lombok.Data;

/**
 * java属性信息
 */
@Builder
@Data
public class ModelProperty {
    /**
     * 下标
     */
    private int index;
    /**
     * 属性名称
     */
    private String name;
    /**
     * 属性注释
     */
    private String remark;
    /**
     * 实体类型
     */
    private String type;
}