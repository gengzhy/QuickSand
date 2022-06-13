package xin.cosmos.basic.generator;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import xin.cosmos.basic.easyexcel.helper.EasyExcelHelper;
import xin.cosmos.basic.generator.model.Model;
import xin.cosmos.basic.generator.model.ModelProperty;
import xin.cosmos.report.entity.InterBankIn;

import java.io.*;
import java.util.List;

/**
 * 实体模板生成测试
 */
public class Test {
    static Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setTemplateLoader(new ClassTemplateLoader(Test.class, "/generator"));
        cfg.setDefaultEncoding("UTF-8");
    }


    public static void main(String[] args) throws IOException, TemplateException {
        //generate();
        String path = "C:\\Users\\geng\\Desktop\\金融台账数据\\台账\\表二：2022存单.xlsx";
        List<InterBankIn> interBankIns = EasyExcelHelper.doReadExcelData(new File(path), InterBankIn.class);
        System.out.println(interBankIns);
    }

    static void generate() throws IOException, TemplateException {
        File template = new File("E:\\IdeaProjects\\QuickSand\\src\\main\\resources\\report\\表二.xlsx");
        List<ModelProperty> properties = ExcelHeadRowToPropertiesGenerator.generate(template, 0, 1, 16);
        Model model = Model.builder()
                .packageName("xin.cosmos.report.entity")
                .entityName("InterBankIn")
                .props(properties)
                .build();

        Template modelTemplate = cfg.getTemplate("ModelExcel.java.ftl");
        File file = new File("E:\\IdeaProjects\\QuickSand\\src\\main\\java\\xin\\cosmos\\report\\entity");
        if (!file.exists()) {
            file.mkdirs();
        }
        String name = modelTemplate.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        String suffix = name.substring(name.lastIndexOf('.'));
        String fileName = file.getAbsolutePath() + "/" + model.getEntityName() + suffix;
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
        modelTemplate.process(model, out);
        out.flush();
        out.close();
    }



}
