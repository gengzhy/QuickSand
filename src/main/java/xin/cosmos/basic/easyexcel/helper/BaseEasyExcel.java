package xin.cosmos.basic.easyexcel.helper;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.easyexcel.template.HeadVO;
import xin.cosmos.basic.util.BeanMapUtil;
import xin.cosmos.basic.util.ObjectsUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * Excel处理父类
 */
@Slf4j
public class BaseEasyExcel {

    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_XLSX = ".xlsx";
    protected static final String EXCEL_KEY = "excel_key";

    /**
     * 设置response响应的Excel相关属性
     *
     * @param response      响应
     * @param excelFileName excel文件名
     */
    protected static void setExcelHttpServletResponseAttributes(HttpServletResponse response, String excelFileName) throws UnsupportedEncodingException {
        if (!isExcelFile(excelFileName)) {
            UnsupportedOperationException ex = new UnsupportedOperationException("不支持当前格式文件");
            log.error("不支持当前格式文件", ex);
            throw ex;
        }
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easy excel没有关系
        String urlFileName = URLEncoder.encode(excelFileName, "UTF-8").replaceAll("\\+", "%20");
        response.addHeader("content-disposition", "attachment;filename*=utf-8''" + urlFileName);
        response.addHeader("excel-name", urlFileName);
        // 将content-disposition和excel-name暴露给前端，否则前端无法取到header里面的属性
        response.addHeader("access-control-expose-headers", "content-disposition");
        response.addHeader("access-control-expose-headers", "excel-name");
        response.addHeader(EXCEL_KEY, excelFileName);
    }

    /**
     * 写文件异常响应异常数据
     *
     * @param response
     * @param errMsg
     */
    @SneakyThrows
    protected static void writeErrMsg(HttpServletResponse response, String errMsg) {
        try {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JSON.toJSONString(ResultVO.failed(errMsg)));
        } catch (Exception ignored) {}
    }

    /**
     * 判断是否是Excel文件
     *
     * @param excelFileName
     * @return
     */
    protected static boolean isExcelFile(String excelFileName) {
        if (ObjectsUtil.isNull(excelFileName)) {
            return false;
        }
        int dotIndex = excelFileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return false;
        }
        String suffix = excelFileName.substring(dotIndex);
        return EXCEL_XLSX.equalsIgnoreCase(suffix) || EXCEL_XLS.equalsIgnoreCase(suffix);
    }

    /**
     * 判断是否是03版本excel
     *
     * @param excelFileName
     * @return
     */
    protected static boolean isXlsExcelFile(String excelFileName) {
        if (ObjectsUtil.isNull(excelFileName)) {
            return false;
        }
        int dotIndex = excelFileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return false;
        }
        String suffix = excelFileName.substring(dotIndex);
        return EXCEL_XLS.equalsIgnoreCase(suffix);
    }

    /**
     * 判断是否是07及以上版本excel
     *
     * @param excelFileName
     * @return
     */
    protected static boolean isXlsxExcelFile(String excelFileName) {
        if (ObjectsUtil.isNull(excelFileName)) {
            return false;
        }
        int dotIndex = excelFileName.lastIndexOf(".");
        String suffix = excelFileName.substring(dotIndex);
        return EXCEL_XLSX.equalsIgnoreCase(suffix);
    }

    /**
     * EasyExcel支持动态列写数据
     *
     * @param builder     指定输出方式和样式
     * @param entityClazz 实体的Class对象
     * @param data        Excel行数据
     */
    public static <T> void doWriteWithDynamicColumns(ExcelWriterSheetBuilder builder, Class<T> entityClazz, List<T> data) {
        List<HeadVO> customizeHeads = new ArrayList<>();
        Field[] fieldArray = entityClazz.getDeclaredFields();
        // 获取类的注解
        for (Field field : fieldArray) {
            // 忽略导出属性
            if (field.isAnnotationPresent(ExcelIgnore.class)) {
                continue;
            }
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                List<String> head = Arrays.asList(excelProperty.value());
                int index = excelProperty.index();
                int order = excelProperty.order();
                HeadVO headVO = HeadVO.builder().headTitle(head).index(index).order(order).field(field.getName()).build();
                customizeHeads.add(headVO);
            }
        }

        // 表头排序
        Collections.sort(customizeHeads);

        // 处理表头
        List<List<String>> heads = new ArrayList<>();
        List<String> fields = new ArrayList<>();
        for (int i = 0; i <= customizeHeads.size() - 1; i++) {
            heads.add(customizeHeads.get(i).getHeadTitle());
            fields.add(customizeHeads.get(i).getField());
        }

        // 处理数据
        List<List<Object>> objs = new ArrayList<>();
        List<Map<String, ?>> maps = BeanMapUtil.beansToMaps(data);
        maps.forEach(map -> {
            List<Object> obj = new ArrayList<>();
            for (String field : fields) {
                obj.add(map.get(field));
            }
            objs.add(obj);
        });
        builder.head(heads).doWrite(objs);
    }
}
