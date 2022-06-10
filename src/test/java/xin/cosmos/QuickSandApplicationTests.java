package xin.cosmos;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import xin.cosmos.basic.base.RedisService;
import xin.cosmos.basic.easyexcel.helper.EasyExcelTemplateFillHelper;
import xin.cosmos.basic.ssh2.Ssh2Service;
import xin.cosmos.basic.util.FileUtils;
import xin.cosmos.report.entity.G01FillModel;

import javax.sql.DataSource;
import java.io.InputStream;
import java.math.BigDecimal;
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
        String targetFilePath = "e:/G01" + System.currentTimeMillis() + ".xls";
        String templatePath = "e:/G01_template.xls";
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

        EasyExcelTemplateFillHelper.fill(templatePath, targetFilePath, model);
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
                if (!(holder instanceof ReadSheetHolder)) {
                    System.out.println(JSON.toJSONString(data, SerializerFeature.WriteMapNullValue));
                    return;
                }
                ReadSheetHolder sheetHolder = (ReadSheetHolder) holder;
                //XlsReadSheetHolder sheetHolder = (XlsReadSheetHolder) holder;
                Map<Integer, Cell> cellMap = sheetHolder.getCellMap();
                int loopSize = Math.min(cellMap.size(), data.size());
                for (int cellIndex = 0; cellIndex < loopSize; cellIndex++) {
                    ReadCellData<?> cell = (ReadCellData<?>) cellMap.get(cellIndex);
                    switch (cell.getType()) {
                        // 字符串
                        case STRING:
                        case DIRECT_STRING:
                        case RICH_TEXT_STRING:
                            System.out.print(AnsiOutput.toString(AnsiColor.RED,cell.getStringValue()) +",");
                            break;
                        // 日期
                        case DATE:
                            System.out.print(AnsiOutput.toString(AnsiColor.GREEN,cell.getStringValue()) +",");
                            break;
                        // 数字
                        case NUMBER:
                            System.out.print(AnsiOutput.toString(AnsiColor.BLUE,cell.getStringValue()) +",");
                            break;
                        // 布尔值
                        case BOOLEAN:
                            System.out.print(AnsiOutput.toString(AnsiColor.CYAN,cell.getStringValue()) +",");
                            break;
                        default:
                            System.out.print(cell.getStringValue() + ",");
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
