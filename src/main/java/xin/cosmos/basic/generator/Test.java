package xin.cosmos.basic.generator;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import xin.cosmos.basic.easyexcel.helper.EasyExcelHelper;
import xin.cosmos.basic.generator.model.Model;
import xin.cosmos.basic.generator.model.ModelProperty;
import xin.cosmos.report.entity.Deposit_IssuanceInterBank;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
        // 生成Java entity
        generate();

        // 读取Excel文件数据
//        String path = "F:\\GRCB\\金融市场部报表数据\\台账\\表二：2022存单.xlsx";
//        List<Deposit_IssuanceInterBank> interBankIns = EasyExcelHelper.doReadExcelData(new File(path), Deposit_IssuanceInterBank.class);
//        interBankIns.forEach(e -> {
//            System.out.println(e);
//            System.out.println("--------------------------------------------------------------------------");
//        });

    }

    static void generate() throws IOException, TemplateException {
        File template = new File("F:\\GRCB\\金融市场部报表数据\\台账\\表二：2022存单.xlsx");
        List<ModelProperty> properties = ExcelHeadRowToPropertiesGenerator.generate(template, 0, 1, 16);
        Model model = Model.builder()
                .packageName("xin.cosmos.report.entity")
                .entityName("Deposit_IssuanceInterBank")
                .entityDesc("发行同业存单-页签")
                .tableName("deposit_issuance_inter_bank")
                .tablePrefix("fr_")
                .props(properties)
                .build();

        Template modelTemplate =  cfg.getTemplate("entity_excel.java.ftl");
        File file = new File("f:/");
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
