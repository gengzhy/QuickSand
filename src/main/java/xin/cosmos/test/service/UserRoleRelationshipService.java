package xin.cosmos.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.cosmos.test.dao.UserRoleRelationshipDao;
import xin.cosmos.test.entity.UserRoleRelationship;

import javax.transaction.Transactional;

@Slf4j
@Service
public class UserRoleRelationshipService {
    @Autowired
    private UserRoleRelationshipDao userRoleRelationshipDao;

    @Transactional
    public UserRoleRelationship testTx(Long userId, Long roleId) {
        return this.saveJpa(userId, roleId);
    }

    /**
     * 新增
     * @param userId
     * @param roleId
     * @return
     */
    private UserRoleRelationship saveJpa(Long userId, Long roleId) {
        if (userRoleRelationshipDao.existsByUserId(userId)) {
            if (userRoleRelationshipDao.existsByUserIdAndRoleId(userId, roleId)) {
                return null;
            }
            userRoleRelationshipDao.deleteByUserId(userId);
        }
        UserRoleRelationship userRoleRelationship = new UserRoleRelationship();
        userRoleRelationship.setUserId(userId);
        userRoleRelationship.setRoleId(roleId);
        return userRoleRelationshipDao.save(userRoleRelationship);
    }
}
