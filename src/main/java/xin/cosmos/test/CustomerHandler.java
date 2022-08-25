package xin.cosmos.test;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import xin.cosmos.basic.util.FileUtils;
import xin.cosmos.test.model.CustomerV1;
import xin.cosmos.test.model.CustomerV3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

// 客户数据处理器
public class CustomerHandler {
    public static void main(String[] args) throws IOException {
//        doWriteV3();
//        doWriteV1();
//        FileUtils.modifyFileDate("C:/Users/geng/Desktop/meta/output/客户信息v1_20220822220417.json", LocalDate.of(2022, Month.AUGUST, 7));
//        FileUtils.modifyFileDate("C:\\Users\\geng\\Desktop\\meta\\doc_v1\\doc_v1", 10);
        FileUtils.modifyFileDate("C:\\Users\\geng\\Desktop\\meta\\doc_v1\\doc_v1", LocalDateTime.now());
    }

    // 修改文件的修改时间
    public static boolean modifyFileDate(String absoluteFilePath, LocalDateTime date) {
        File file = new File(absoluteFilePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    modifyFileDate(f.getAbsolutePath(), date);
                }
            }
        }
        Calendar calendar = Calendar.getInstance();
        int month = date.getMonthValue() - 1;
        calendar.set(date.getYear(), month, date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
        return file.setLastModified(calendar.getTimeInMillis());
    }

    public static boolean modifyFileDate(String absoluteFilePath, int diffDays) {
        File file = new File(absoluteFilePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    modifyFileDate(f.getAbsolutePath(), diffDays);
                }
            }
        }
        long diffMillis = file.lastModified() - 24L * 60 * 60 * 1000 * diffDays;
        return file.setLastModified(diffMillis);
    }

    // 客户信息处理V1版本
    static void doWriteV1() throws IOException {
        String version = "v1";
        String filePath = "C:\\Users\\geng\\Desktop\\meta\\v1\\客户信息" + version + ".xlsx";
        String toPath = "C:/Users/geng/Desktop/meta/output/v1/客户信息" + version + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".json";
        File file = new File(filePath);
        List<CustomerV1> metas = ReadExcelUtils.readExcel(file, CustomerV1.class);
        metas.forEach(c -> {
            // 类型
            c.setC4("查询");
            //todo 文书编号、查冻扣原由
            if (c.getC60() != null) {
                String[] s1 = c.getC60().split("/");
                if (s1.length > 1) {
                    s1[1] = Integer.parseInt(s1[1]) < 10 ? "0"+s1[1] : s1[1];
                    s1[2] = Integer.parseInt(s1[2]) < 10 ? "0"+s1[2] : s1[2];
                    c.setC60(s1[0] + s1[1] + s1[2]);
                }
            }
            if (c.getC61() != null) {
                String[] s2 = c.getC61().split("/");
                if (s2.length > 1) {
                    s2[1] = Integer.parseInt(s2[1]) < 10 ? "0"+s2[1] : s2[1];
                    s2[2] = Integer.parseInt(s2[2]) < 10 ? "0"+s2[2] : s2[2];
                    c.setC61(s2[0] + s2[1] + s2[2]);
                }
            }
        });
        metas = metas.stream().filter(c -> c.getC14() != null).collect(Collectors.toList());
        File targetFile = new File(toPath);
        IOUtils.write(JSON.toJSONString(metas), new FileOutputStream(targetFile), Charsets.toCharset("UTF-8"));
    }

    // 客户信息处理V3版本
    static void doWriteV3() throws IOException {
        String version = "v3";
        String filePath = "C:/Users/geng/Desktop/meta/客户信息/v3" + version + ".xlsx";
        String toPath = "C:/Users/geng/Desktop/meta/output/v3/客户信息" + version + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".json";
        File file = new File(filePath);
        List<CustomerV3> metas = ReadExcelUtils.readExcel(file, CustomerV3.class);
        metas.forEach(c -> {
            c.setC61(c.getC9());
            try {
                c.setC60(getPrevMonthDate(toDate(c.getC61()), 1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        File targetFile = new File(toPath);
        IOUtils.write(JSON.toJSONString(metas), new FileOutputStream(targetFile), Charsets.toCharset("UTF-8"));
    }

    static Date toDate(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyyMMdd").parse(date);
    }

    /*
     * 计算日期的上一个月
     */
    static String getPrevMonthDate(Date date, int n) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - n);
        return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
    }

    // 客户信息处理V1版本
    static <T> void doWrite(String version, Class<T> entityClazz) throws IOException {
        String filePath = "C:/Users/geng/Desktop/meta/客户信息" + version + ".xlsx";
        String toPath = "C:/Users/geng/Desktop/meta/output/客户信息" + version + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".json";
        File file = new File(filePath);
        List<T> metas = ReadExcelUtils.readExcel(file, entityClazz);
        File targetFile = new File(toPath);
        IOUtils.write(JSON.toJSONString(metas), new FileOutputStream(targetFile), Charsets.toCharset("UTF-8"));
    }
}

class ReadExcelUtils {
    public static <T> List<T> readExcel(File excelFile, Class<T> entityClass) {
        List<T> data = new LinkedList<>();
        EasyExcelFactory.read(excelFile, entityClass, new ReadListener<T>(data::addAll)).sheet().doRead();
        return data;
    }
}

class ReadListener<T> extends PageReadListener<T> {
    private List<T> cache = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private final Consumer<List<T>> consumer;

    public ReadListener(Consumer<List<T>> consumer) {
        super(consumer);
        this.consumer = consumer;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        // 如果一行Excel数据均为空值，则不装载该行数据
        if (lineNull(data)) {
            return;
        }
        cache.add(data);
        if (cache.size() >= BATCH_COUNT) {
            consumer.accept(cache);
            cache = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (cache == null || cache.isEmpty()) {
            return;
        }
        consumer.accept(cache);
    }

    boolean lineNull(T line) {
        if (line instanceof String) {
            return StringUtils.isEmpty((String) line);
        }
        try {
            Set<Field> fields = Arrays.stream(line.getClass().getDeclaredFields()).filter(f -> f.isAnnotationPresent(ExcelProperty.class)).collect(Collectors.toSet());
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(line) != null) {
                    return false;
                }
            }
            return true;
        } catch (Exception ignored) {
        }
        return true;
    }

}
