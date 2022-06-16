package xin.cosmos.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.easyexcel.helper.EasyExcelTemplateFillHelper;
import xin.cosmos.basic.generator.param.EntityModelParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = "代码生成-Generator")
@Controller
public class GeneratorController {

    @ResponseBody
    @ApiOperation(value = "代码生成")
    @GetMapping(value = "generate")
    public void download(HttpServletResponse response, MultipartFile excelFile, EntityModelParam param) throws IOException {
        String targetFileName = "G01" + System.currentTimeMillis() + ".xls";
        String templatePath = "report/G01_template.xls";
        ClassPathResource resource = new ClassPathResource(templatePath);
        EasyExcelTemplateFillHelper.fillToResponse(response, resource.getInputStream(), null, targetFileName);
    }

}
