package xin.cosmos;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.holder.xls.XlsReadSheetHolder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import xin.cosmos.basic.base.RedisService;
import xin.cosmos.basic.ssh2.Ssh2Service;
import xin.cosmos.report.entity.G01FillModel;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class QuickSandApplicationTests {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        System.out.println(dataSource.getClass());
        Map<String, Object> map = jdbcTemplate.queryForMap("select now() from dual");
        map.forEach((k, v) -> System.out.println(v));
    }

    @Autowired
    private Ssh2Service ssh2Service;

    @Test
    public void testShh2() {
        String command = "pwd";
        ssh2Service.execute(command);
    }

    @Autowired
    private RedisService redisService;

    @Test
    public void testBackUp() {
        redisService.syncSave();
    }

    /**
     * Excel填充数据测试
     */
    @Test
    public void testFillExcelTemplate() {
        String templatePath = "report/G01_template.xls";
        String file = "F:/G01" + System.currentTimeMillis() + ".xls";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath);
        G01FillModel model = G01FillModel.builder()
                .g_6_a(100D).g_7_a(80D).g_8_a(99D).g_9_a(1100D).g_10_a(1000D)
                .g_18_a(100D).g_19_a(80D).g_20_a(99D).g_21_a(1100D).g_22_a(1000D)
                .build();
        ExcelWriter writer = EasyExcel.write(file).withTemplate(inputStream).build();
        Workbook workbook = writer.writeContext().writeWorkbookHolder().getWorkbook();
        // 必须设置强制计算公式：不然公式会以字符串的形式显示在excel中，而不进行相关的运算
        workbook.setForceFormulaRecalculation(true);

        // 填充数据
        WriteSheet sheet = EasyExcel.writerSheet(0).build();
        writer.fill(model, sheet);
        writer.finish();
        System.out.println("执行完毕");
    }

    @Test
    public void testEasyExcel() {
        String path = "report/G01_template.xls";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        EasyExcel.read(inputStream, new AnalysisEventListener<LinkedHashMap<Integer, Object>>() {
            @Override
            public void invoke(LinkedHashMap<Integer, Object> data, AnalysisContext context) {
                ReadHolder holder = context.currentReadHolder();
                if (!(holder instanceof XlsReadSheetHolder)) {
                    System.out.println(JSON.toJSONString(data, SerializerFeature.WriteMapNullValue));
                    return;
                }
                XlsReadSheetHolder sheetHolder = (XlsReadSheetHolder) holder;
                Map<Integer, Cell> cellMap = sheetHolder.getCellMap();
                int loopSize = Math.min(cellMap.size(), data.size());
                for (int cellIndex = 0; cellIndex < loopSize; cellIndex++) {
                    ReadCellData cell = (ReadCellData) cellMap.get(cellIndex);
                    switch (cell.getType()) {
                        // 字符串
                        case STRING:
                        case DIRECT_STRING:
                        case RICH_TEXT_STRING:
                            System.out.print("字符串：" + cell.getStringValue() + ",");
                            break;
                        // 日期
                        case DATE:
                            System.out.print("日期：" + cell.getStringValue() + ",");
                            break;
                        // 数字
                        case NUMBER:
                            System.out.print("数字：" + cell.getNumberValue() + ",");
                            break;
                        // 布尔值
                        case BOOLEAN:
                            System.out.print("布尔值：" + cell.getBooleanValue() + ",");
                            break;
                        default:
                            System.out.print("字符串：" + cell.getData() + ",");
                    }
                }
                System.out.println();
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {

            }
        }).sheet(0).autoTrim(true).headRowNumber(3).doRead();
    }

}
