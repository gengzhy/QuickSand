package xin.cosmos.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.cosmos.service.BillAcceptanceApiService;

@Api(tags = "票据承兑人信息查询Controller")
@RestController
public class BillAcceptanceController {
    @Autowired
    private BillAcceptanceApiService billAcceptanceApiService;


    @ApiOperation(value = "querySomething测试")
    @GetMapping(value = "querySomething")
    public String querySomething() {
        return billAcceptanceApiService.querySomething("hello querySomething is something").getData();
    }
}
