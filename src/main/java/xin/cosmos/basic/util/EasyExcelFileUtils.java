package xin.cosmos.basic.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import xin.cosmos.basic.define.ResultVO;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * EasyExcel文件处理工具
 */
@Slf4j
public class EasyExcelFileUtils {

    /**
     * Excel数据浏览器下载
     * <p>
     * 注：参数-excelDataType 需要自定义实体，且在实体类中使用注解 @{@linkplain com.alibaba.excel.annotation.ExcelProperty}指定导出的列的标题名称
     * e.g:
     *
     * <pre>
     *      public class Person {
     *         @ExcelProperty(value="姓名")
     *         private String name;
     *
     *         // getter/setter省略
     *     }
     * </pre>
     *
     * @param fileName      下载文件名称
     * @param response      响应容器
     * @param data          需下载数据
     * @param excelDataType 下载数据类型模板
     */
    @SneakyThrows
    public static void downloadExcel(String fileName, HttpServletResponse response, List<?> data, Class<?> excelDataType) {
        if (ObjectsUtil.isNull(data)) {
            log.error("写文件错误：{}", "暂无可下载的数据");
            writeErrMsg(response, "暂无可下载的数据");
            return;
        }
        try {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String urlFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + urlFileName + ".xlsx");
            response.addHeader("excel-file-name", urlFileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), excelDataType)
                    .excelType(ExcelTypeEnum.XLSX)
                    .autoCloseStream(true)
                    .sheet("sheet1")
                    .doWrite(data);
        } catch (Exception e) {
            log.error("写文件错误：{}", e.getMessage());
            writeErrMsg(response, e.getMessage());
        }
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
