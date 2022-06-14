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
    private String entityDesc;

    /**
     * 指定实体映射的数据库表名称
     * <p>
     * 注意：该属性的优先级高于entityName。
     * 当指定了tableName，表名就是tableName的属性值。
     * 当该属性值为空时，表名默认为entityName的值，下划线分隔全小写
     */
    private String tableName;

    /**
     * 指定实体映射的数据库表的前缀
     * <p>
     * 如果不指定，则没有表名前缀
     */
    private String tablePrefix;

    /**
     * 实体名称
     */
    private List<ModelProperty> props;
}
