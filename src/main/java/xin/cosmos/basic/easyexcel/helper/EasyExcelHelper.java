package xin.cosmos.basic.easyexcel.helper;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.easyexcel.template.HeadVO;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.util.BeanMapUtil;
import xin.cosmos.basic.util.ObjectsUtil;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * EasyExcel 帮助类
 */
@Slf4j
public class EasyExcelHelper {

    /**
     * Excel数据浏览器下载
     *
     * @param pathName    下载文件的完整路径名称
     * @param data        需下载数据
     * @param entityClazz 下载数据类型模板
     */
    @SneakyThrows
    public static <T> void downloadExcel(String pathName, List<T> data, Class<T> entityClazz) {
        try {
            // 构建Excel表头及数据体
            ExcelWriterSheetBuilder builder = EasyExcel.write(pathName)
                    .autoCloseStream(true)
                    .sheet("sheet1");
            doWriteWithDynamicColumns(builder, entityClazz, data);
        } catch (Exception e) {
            log.error("写文件错误：{}", e.getMessage());
            throw new PlatformException("Excel下载数据错误");
        }
    }


    /**
     * Excel数据浏览器下载
     * <p>
     * 注：参数-excelDataType 需要自定义实体，且在实体类中使用注解 @{@linkplain com.alibaba.excel.annotation.ExcelProperty}指定导出的列的标题名称
     * e.g:
     * public class Person {
     *
     * @param excelFileName 下载文件名称
     * @param response      响应容器
     * @param data          需下载数据
     * @param entityClazz   下载数据Bean实体类型
     * @ExcelProperty(value="姓名") private String name;
     * // getter/setter省略
     * }
     */
    public static <T> void downloadExcelToResponse(HttpServletResponse response, String excelFileName, List<T> data, Class<T> entityClazz) {
        if (ObjectsUtil.isNull(data)) {
            log.error("写文件错误：{}", "暂无可下载的数据");
            writeErrMsg(response, "暂无可下载的数据");
            return;
        }
        try {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            if (excelFileName.endsWith(".xlsx") || excelFileName.endsWith(".xls") ||
                    excelFileName.endsWith(".XLSX") || excelFileName.endsWith(".XLS")) {
                excelFileName = excelFileName.substring(0, excelFileName.lastIndexOf("."));
            }
            // 这里URLEncoder.encode可以防止中文乱码 当然和easy excel没有关系
            String urlFileName = URLEncoder.encode(excelFileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + urlFileName + ".xlsx");
            response.setHeader("excel-file-name", urlFileName + ".xlsx");

            // 构建Excel表头及数据体
            ExcelWriterSheetBuilder builder = EasyExcel.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .autoCloseStream(true)
                    .sheet("sheet1");
            doWriteWithDynamicColumns(builder, entityClazz, data);
        } catch (Exception e) {
            log.error("写文件错误：{}", e.getMessage());
            writeErrMsg(response, e.getMessage());
        }
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


    @SneakyThrows
    private static void writeErrMsg(HttpServletResponse response, String errMsg) {
        // 重置response
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println(JSON.toJSONString(ResultVO.failed(errMsg)));
    }
}
