package xin.cosmos.test.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import xin.cosmos.test.entity.UserRoleRelationship;
import xin.cosmos.test.entity.pk.PkUserRoleRelationship;

import java.util.List;

public interface UserRoleRelationshipDao extends JpaRepository<UserRoleRelationship, PkUserRoleRelationship> {
    /**
     * 根据userId查询
     * @param userId
     * @return
     */
    List<UserRoleRelationship> findUserRoleRelationshipByUserId(Long userId);

    /**
     * 是否存在
     * @param userId 用户Id
     * @return
     */
    boolean existsByUserId(Long userId);

    /**
     * 是否存在
     * @param userId 用户Id
     * @param roleId 角色Id
     * @return
     */
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * 通过userId删除
     * @param userId
     * @return
     */
    int deleteByUserId(Long userId);

    @Modifying
    @Query(value = "insert into u_user_role_relationship(user_id,role_id,version) values(?1,?2,?3)", nativeQuery = true)
    int add(Long userId, Long roleId, int version);
}
