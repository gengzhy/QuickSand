package xin.cosmos.test.entity.pk;

import lombok.Data;

import java.io.Serializable;

@Data
public class PkUserRoleRelationship implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long roleId;
}
