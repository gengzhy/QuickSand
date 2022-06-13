package xin.cosmos.basic.generator.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 生成实体类的相关属性
 */
@Builder
@Data
public class Model {
    /**
     * 实体包路径
     */
    private String packageName;
    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 实体名称
     */
    private List<ModelProperty> props;
}
