package xin.cosmos.report.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import xin.cosmos.basic.base.RedisService;
import xin.cosmos.report.service.FinancialReportService;

@Api(tags = "金融报表-Controller")
@Controller
@RequestMapping(value = "report")
public class FinancialReportController {
    @Autowired
    private FinancialReportService financialReportService;
    @Autowired
    private RedisService redisService;
    //todo
}
