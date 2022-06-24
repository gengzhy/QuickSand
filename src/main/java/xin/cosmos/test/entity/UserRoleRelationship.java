package xin.cosmos.test.entity;

import lombok.Data;
import org.apache.poi.ss.formula.ptg.Ptg;
import xin.cosmos.test.entity.pk.PkUserRoleRelationship;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(value = PkUserRoleRelationship.class)
@Table(name = "u_user_role_relationship")
public class UserRoleRelationship implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long userId;
    @Id
    private Long roleId;

    @Version
    private Long version;
}
