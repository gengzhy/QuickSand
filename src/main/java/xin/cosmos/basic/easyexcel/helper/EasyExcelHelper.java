package xin.cosmos.basic.easyexcel.helper;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.easyexcel.framework.BatchPageReadListener;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.util.ObjectsUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * EasyExcel 帮助类
 */
@Slf4j
public class EasyExcelHelper extends BaseEasyExcel {

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
            setExcelHttpServletResponseAttributes(response, excelFileName);

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

}
