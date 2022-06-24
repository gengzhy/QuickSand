package xin.cosmos.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.test.entity.UserRoleRelationship;
import xin.cosmos.test.service.UserRoleRelationshipService;

@Api(tags = "测试-Test")
@RequestMapping(value = "test")
@Controller
public class TestController {

    @Autowired
    private UserRoleRelationshipService userRoleRelationshipService;

    @ResponseBody
    @ApiOperation(value = "事务测试")
    @PostMapping(value = "testTx")
    public ResultVO<UserRoleRelationship> testTx(@RequestParam(value = "userId") Long userId, @RequestParam(value = "roleId")Long roleId) {
        return ResultVO.success(userRoleRelationshipService.testTx(userId, roleId));
    }

}
