package xin.cosmos.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.cosmos.basic.api.param.FindSettlePageParam;
import xin.cosmos.basic.api.vo.AccInfoListByAcptNameVO;
import xin.cosmos.basic.api.vo.FindSettlePageVO;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.define.SingleParam;
import xin.cosmos.service.BillAcceptanceApiService;

import javax.validation.Valid;

@Api(tags = "票据承兑人信息查询-Controller")
@RestController
@RequestMapping(value = "bill/disclosure")
public class BillAcceptanceController {
    @Autowired
    private BillAcceptanceApiService billAcceptanceApiService;

    @ApiOperation(value = "根据票据承兑人名称查询票据承兑人信息列表")
    @ApiImplicitParam(name = "acceptName", value = "票据承兑人名称")
    @PostMapping(value = "findAccInfoListByAcptName")
    public ResultVO<AccInfoListByAcptNameVO> findAccInfoListByAcptName(@Valid @RequestBody SingleParam<String> acceptName) {
        return billAcceptanceApiService.findAccInfoListByAcptName(acceptName);
    }

    @ApiOperation(value = "票据承兑信用信息披露查询")
    @PostMapping(value = "findSettlePage")
    public ResultVO<FindSettlePageVO> findSettlePage(@Valid @RequestBody FindSettlePageParam param) {
        return billAcceptanceApiService.findSettlePage(param);
    }
}
