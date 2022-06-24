package xin.cosmos.basic.generator;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.listener.PageReadListener;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import xin.cosmos.basic.easyexcel.framework.BatchPageReadListener;
import xin.cosmos.basic.generator.model.Model;
import xin.cosmos.basic.generator.model.ModelProperty;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
//        generate();

        // 读取Excel文件数据
//        String path = "F:\\GRCB\\金融市场部报表数据\\台账\\表二：2022存单.xlsx";
//        List<Deposit_IssuanceInterBank> interBankIns = EasyExcelHelper.doReadExcelData(new File(path), Deposit_IssuanceInterBank.class);
//        interBankIns.forEach(e -> {
//            System.out.println(e);
//            System.out.println("--------------------------------------------------------------------------");
//        });

        List<LinkedHashMap<Integer, Object>> data = new LinkedList<>();
        String file = "F:\\docs\\贵阳农商银行2022年互联网金融不良贷款呆账核销清单220621.xlsx";
        EasyExcelFactory.read(new File(file), new PageReadListener<LinkedHashMap<Integer, Object>>(list -> {
            data.addAll(list);
        })).headRowNumber(5).sheet().doRead();
        System.out.println(data);

        Map<String, String> studentData = new LinkedHashMap<>();
        EasyExcelFactory.read(new File(file), new PageReadListener<LinkedHashMap<Integer, Object>>(list -> {
            list.forEach(e -> {
                String v = (String) e.get(0);
                studentData.put(v, v);
            });
        })).headRowNumber(1).sheet(1).doRead();

        Map<String, String> officialData = new LinkedHashMap<>();
        EasyExcelFactory.read(new File(file), new PageReadListener<LinkedHashMap<Integer, Object>>(list -> {
            list.forEach(e -> {
                String v = (String) e.get(0);
                officialData.put(v, v);
            });
        })).headRowNumber(1).sheet(2).doRead();

        int typeIndex = 21;
        int codeIndex = 3;
        data.forEach(map -> {
            String code = (String) map.get(codeIndex);
            if (studentData.get(code) != null) {
                map.put(typeIndex, "大学生");
            } else if (officialData.get(code) != null) {
                map.put(typeIndex, "公务员");
            } else {
                map.put(typeIndex, null);
            }
        });

        String toPath = "F:/docs/贵阳农商银行2022年互联网金融不良贷款呆账核销清单"+System.currentTimeMillis()+".xlsx";
        EasyExcel.write(toPath)
                .autoCloseStream(true)
                .sheet("sheet1")
                .doWrite(data);
    }

    static void generate() throws IOException, TemplateException {
        File template = new File("F:\\GRCB\\金融市场部报表数据\\台账\\表二：2022存单.xlsx");
        List<ModelProperty> properties = ExcelHeadRowToPropertiesGenerator.generate(template, 0, 1, 16);
        Model model = Model.builder()
                .packageName("xin.cosmos.report.entity")
                .entityName("DepositIssuanceInterBank")
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
