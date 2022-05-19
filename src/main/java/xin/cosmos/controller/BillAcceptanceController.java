package xin.cosmos.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xin.cosmos.basic.api.vo.AccInfoListByAcptNameVO;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.service.BillAcceptanceApiService;

@Api(tags = "票据承兑人信息查询-Controller")
@RestController
public class BillAcceptanceController {
    @Autowired
    private BillAcceptanceApiService billAcceptanceApiService;

    @ApiOperation(value = "根据票据承兑人名称查询票据承兑人信息列表")
    @ApiImplicitParam(name = "acceptName", value = "票据承兑人名称", paramType = "query", required = true)
    @GetMapping(value = "findAccInfoListByAcptName")
    public ResultVO<AccInfoListByAcptNameVO> findAccInfoListByAcptName(@RequestParam("acceptName") String acceptName) {
        return billAcceptanceApiService.findAccInfoListByAcptName(acceptName);
    }
}
