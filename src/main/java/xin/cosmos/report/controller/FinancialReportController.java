package xin.cosmos.report.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.cosmos.basic.base.RedisService;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.easyexcel.helper.EasyExcelHelper;
import xin.cosmos.report.entity.DepositIssuanceInterBank;
import xin.cosmos.report.service.FinancialReportService;

import java.io.File;
import java.util.List;

@Api(tags = "金融报表-Controller")
@RestController
@RequestMapping(value = "report")
public class FinancialReportController {
    @Autowired
    private FinancialReportService financialReportService;
    @Autowired
    private RedisService redisService;

    @ApiOperation(value = "新增发行同业存单元数据")
    @PostMapping(value = "/add/all/DepositIssuanceInterBank")
    public ResultVO<?> addAlDepositIssuanceInterBank() {
        List<DepositIssuanceInterBank> interBanks = financialReportService.saveAllDepositIssuanceInterBank(initData());
        return ResultVO.success("共新增发行同业存单元数据[" + interBanks.size() + "]条");
    }

    private List<DepositIssuanceInterBank> initData() {
        String path = "C:\\Users\\geng\\Desktop\\金融台账数据\\台账\\表二：2022存单.xlsx";
        return EasyExcelHelper.doReadExcelData(new File(path), DepositIssuanceInterBank.class);
    }
}
