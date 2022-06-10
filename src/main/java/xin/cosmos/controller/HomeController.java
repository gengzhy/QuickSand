package xin.cosmos.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.cosmos.basic.easyexcel.helper.EasyExcelTemplateFillHelper;
import xin.cosmos.basic.util.FileUtils;
import xin.cosmos.report.entity.G01FillModel;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

@Api(tags = "首页-Home")
@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String toHomePage() {
        return "index";
    }

    @ApiOperation(value = "票据承兑人信用披露信息管理")
    @GetMapping(value = "/bill/info")
    public String toBillInfoPage() {
        return "bill/index";
    }

    @ResponseBody
    @ApiOperation(value = "票据承兑人信用披露信息管理")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) {
        String targetFileName = "G01" + System.currentTimeMillis() + ".xls";
        String templatePath = "report/G01_template.xls";
        EasyExcelTemplateFillHelper.fillToResponse(response, templatePath, data(), targetFileName);
    }

    @ResponseBody
    @GetMapping(value = "/d1")
    public void download1(HttpServletResponse response) throws IOException {
        String targetFileName = "G01报表测试"+System.currentTimeMillis()+".xlsx";
        String template = "e:/G01_template.xlsx";
        EasyExcelTemplateFillHelper.fillToResponse(response, template, data(), targetFileName);
    }
    @ResponseBody
    @GetMapping(value = "/d2")
    public void download2(HttpServletResponse response) throws IOException {
        String targetFileName = "G01报表测试"+System.currentTimeMillis()+".xls";
        String templatePath = "e:/G01_template.xls";
        EasyExcelTemplateFillHelper.fillToResponse(response, templatePath, data(), targetFileName);
    }

    G01FillModel data() {
        BigDecimal ten = BigDecimal.TEN;
        G01FillModel model = G01FillModel.builder()
                .preparer("耿").reviewer("耿").charger("耿")
                .g_6_a(ten).g_7_a(ten).g_8_a(ten).g_9_a(ten).g_10_a(ten)
                .g_18_a(ten).g_19_a(ten).g_20_a(ten).g_21_a(ten).g_22_a(ten)
                .g_26_a(ten).g_28_a(ten).g_31_a(ten).g_32_a(ten).g_33_a(ten).g_34_a(ten).g_35_a(ten).g_36_a(ten)
                .g_37_a(ten).g_42_a(ten).g_56_a(ten).g_64_a(ten).g_65_a(ten).g_66_a(ten).g_67_a(ten).g_68_a(ten)
                .g_71_a(ten).g_72_a(ten).g_73_a(ten).g_74_a(ten).g_75_a(ten).g_78_a(ten).g_79_a(ten).g_80_a(ten)
                .g_81_a(ten).g_82_a(ten).g_83_a(ten).g_84_a(ten).g_107_a(ten).g_134_c(ten).g_135_c(ten).g_137_c(ten)
                .g_138_c(ten).g_136_c(ten)
                .build();
        return model;
    }
}
