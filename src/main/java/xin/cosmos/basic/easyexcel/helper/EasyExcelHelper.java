package xin.cosmos.basic.easyexcel.helper;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.easyexcel.framework.BatchPageReadListener;
import xin.cosmos.basic.easyexcel.template.HeadVO;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.util.BeanMapUtil;
import xin.cosmos.basic.util.ObjectsUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * EasyExcel 帮助类
 */
@Slf4j
public class EasyExcelHelper {

    /**
     * 读取Excel文件
     *
     * @param stream      文件流
     * @param entityClass 读取转换的Java对象类型
     * @param <T>
     * @return
     */
    public static <T> List<T> doReadExcelData(InputStream stream, Class<T> entityClass) {
        List<T> data = new LinkedList<>();
        EasyExcelFactory.read(stream, entityClass, new PageReadListener<T>(data::addAll)).sheet().doRead();
        return data;
    }

    /**
     * 读取Excel文件
     *
     * @param stream      文件流
     * @param entityClass 读取转换的Java对象类型
     * @param comparator  排序比较器
     * @param <T>
     * @return
     */
    public static <T> List<T> doReadExcelData(InputStream stream, Class<T> entityClass, Comparator<T> comparator) {
        List<T> data = new LinkedList<>();
        EasyExcelFactory.read(stream, entityClass, new BatchPageReadListener<T>(list -> {
            if (comparator != null) {
                list.sort(comparator);
            }
            data.addAll(list);
        })).sheet().doRead();
        return data;
    }

    /**
     * 读取Excel文件
     *
     * @param file        MultipartFile文件
     * @param entityClass 读取转换的Java对象类型
     */
    @SneakyThrows
    public static <T> List<T> doReadExcelData(MultipartFile file, Class<T> entityClass) {
        return doReadExcelData(file.getInputStream(), entityClass);
    }

    /**
     * 读取Excel文件
     *
     * @param file        File文件
     * @param entityClass 读取转换的Java对象类型
     */
    @SneakyThrows
    public static <T> List<T> doReadExcelData(File file, Class<T> entityClass) {
        List<T> data = new LinkedList<>();
        EasyExcelFactory.read(file, entityClass, new BatchPageReadListener<T>(data::addAll)).sheet().doRead();
        return data;
    }

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
     * 前端下载js代码示例：
     * <pre>
     * function downloadFile(bytes, fileName) {
     *    const blob = new Blob([bytes], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
     *    if (window.navigator.msSaveOrOpenBlob) { // 兼容IE10
     *          navigator.msSaveBlob(blob, fileName)
     *    } else {
     *         const url = window.URL.createObjectURL(blob);
     *         const a = document.createElement('a');
     *         a.href = url;
     *         a.download = fileName;
     *         a.click();
     *         window.URL.revokeObjectURL(url);
     *    }
     * }
     * </pre>
     *
     * @param excelFileName 下载文件名称
     * @param response      响应容器
     * @param data          需下载数据
     * @param entityClazz   下载数据Bean实体类型，苏醒必须使用注解<code>@ExcelProperty</code>中value指定写出列的表头吗，名称
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
