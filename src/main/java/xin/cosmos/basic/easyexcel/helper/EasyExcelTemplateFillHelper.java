package xin.cosmos.basic.easyexcel.helper;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.easyexcel.framework.BatchPageReadListener;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.util.ObjectsUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * EasyExcel 模板数据填充帮助类
 */
@Slf4j
public class EasyExcelTemplateFillHelper extends BaseEasyExcel {

    /**
     * 根据Excel模板文件，向HttpServletResponse写入目标文件流
     * <p>
     * 目标文件的Excel页签默认写入第一个
     *
     * @param response             响应
     * @param templateAbsolutePath 模板文件
     * @param entityModel          模板数据模型实体
     * @param targetFileName       文件文件名
     */
    public static void fillToResponse(HttpServletResponse response, String templateAbsolutePath, Object entityModel, String targetFileName) {
        fillToResponse(response, new File(templateAbsolutePath), entityModel, targetFileName);
    }

    /**
     * 根据Excel模板文件，向HttpServletResponse写入目标文件流
     * <p>
     * 目标文件的Excel页签默认写入第一个
     *
     * @param response       响应
     * @param templateFile   模板文件
     * @param entityModel    模板数据模型实体
     * @param targetFileName 文件文件名
     */
    public static void fillToResponse(HttpServletResponse response, File templateFile, Object entityModel, String targetFileName) {
        fillToResponse(response, templateFile, entityModel, targetFileName, 0);
    }

    /**
     * 根据Excel模板文件，向HttpServletResponse写入目标文件流
     *
     * @param response       响应
     * @param template       模板文件
     * @param entityModel    模板数据模型实体
     * @param targetFileName 文件文件名
     * @param sheetNo        写入目标文件的Excel 第几个页签（下标从0开始）
     */
    public static void fillToResponse(HttpServletResponse response, String templateAbsolutePath, Object entityModel, String targetFileName, int sheetNo) {
        fillToResponse(response, new File(templateAbsolutePath), entityModel, targetFileName, sheetNo);
    }

    /**
     * 根据Excel模板文件，向HttpServletResponse写入目标文件流
     *
     * @param response       响应
     * @param templateFile   模板文件
     * @param entityModel    模板数据模型实体
     * @param targetFileName 文件文件名
     * @param sheetNo        写入目标文件的Excel 第几个页签（下标从0开始）
     */
    public static void fillToResponse(HttpServletResponse response, File templateFile, Object entityModel, String targetFileName, int sheetNo) {
        try {
            // 设计响应的excel文件属性
            setExcelHttpServletResponseAttributes(response, targetFileName);
            ExcelWriter writer = initExcelWriter(templateFile, response.getOutputStream());
            // 填充数据
            WriteSheet sheet = EasyExcel.writerSheet(sheetNo).build();
            writer.fill(entityModel, sheet);
            writer.finish();
        } catch (Exception ex) {
            writeErrMsg(response, ex.getMessage());
        }
    }

    /**
     * 根据Excel模板文件，向HttpServletResponse写入目标文件流
     *
     * @param response             响应
     * @param templateAbsolutePath 模板文件
     * @param entityModel          模板数据模型实体
     * @param targetFileName       文件文件名
     * @param sheetName            写入目标文件的Excel 页签名称
     */
    public static void fillToResponse(HttpServletResponse response, String templateAbsolutePath, Object entityModel, String targetFileName, String sheetName) {
        fillToResponse(response, new File(templateAbsolutePath), entityModel, targetFileName, sheetName);
    }

    /**
     * 根据Excel模板文件，向HttpServletResponse写入目标文件流
     *
     * @param response       响应
     * @param templateFile   模板文件
     * @param entityModel    模板数据模型实体
     * @param targetFileName 文件文件名
     * @param sheetName      写入目标文件的Excel 页签名称
     */
    public static void fillToResponse(HttpServletResponse response, File templateFile, Object entityModel, String targetFileName, String sheetName) {
        try {
            // 设计响应的excel文件属性
            setExcelHttpServletResponseAttributes(response, targetFileName);
            ExcelWriter writer = initExcelWriter(templateFile, response.getOutputStream());
            // 填充数据
            WriteSheet sheet = EasyExcel.writerSheet(sheetName).build();
            writer.fill(entityModel, sheet);
            writer.finish();
        } catch (Exception ex) {
            writeErrMsg(response, ex.getMessage());
        }
    }

    /**
     * 根据Excel模板文件，写入目标文件流
     *
     * @param templateAbsolutePath 模板文件
     * @param targetAbsolutePath   目标文件
     * @param entityModel          模板数据模型实体
     */
    public static void fill(String templateAbsolutePath, String targetAbsolutePath, Object entityModel) {
        fill(new File(templateAbsolutePath), new File(targetAbsolutePath), entityModel);
    }

    /**
     * 根据Excel模板文件，写入目标文件流
     *
     * @param templateAbsolutePath 模板文件
     * @param targetFile           目标文件
     * @param entityModel          模板数据模型实体
     */
    public static void fill(String templateAbsolutePath, File targetFile, Object entityModel) {
        fill(new File(templateAbsolutePath), targetFile, entityModel);
    }

    /**
     * 根据Excel模板文件，写入目标文件流
     *
     * @param templateFile 模板文件
     * @param targetFile   目标文件
     * @param entityModel  模板数据模型实体
     */
    public static void fill(File templateFile, File targetFile, Object entityModel) {
        try {
            ExcelWriter writer = initExcelWriter(templateFile, targetFile);
            // 填充数据
            WriteSheet sheet = EasyExcel.writerSheet(0).build();
            writer.fill(entityModel, sheet);
            writer.finish();
        } catch (Exception ex) {
            // 写入文件失败，删除创建的空文件
            try {
                Files.deleteIfExists(Paths.get(targetFile.getAbsolutePath()));
            } catch (IOException ignored) {
            }
            throw new PlatformException(ex.getMessage());
        }
    }

    /**
     * 初始化ExcelWriter
     *
     * @param templateFile        模板文件
     * @param targetFileOutStream 输出目标文件
     * @return
     */
    private static ExcelWriter initExcelWriter(File templateFile, OutputStream targetFileOutStream) {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLS;
        if (isXlsxExcelFile(templateFile.getAbsolutePath())) {
            excelType = ExcelTypeEnum.XLSX;
        }
        ExcelWriter writer = EasyExcel.write(targetFileOutStream).withTemplate(templateFile).excelType(excelType).build();
        Workbook workbook = writer.writeContext().writeWorkbookHolder().getWorkbook();
        // 必须设置强制计算公式：不然公式会以字符串的形式显示在excel中，而不进行相关的运算
        workbook.setForceFormulaRecalculation(true);
        return writer;
    }

    /**
     * 初始化ExcelWriter
     *
     * @param templateFile 模板文件
     * @param targetFile   输出目标文件
     * @return
     */
    private static ExcelWriter initExcelWriter(File templateFile, File targetFile) {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLS;
        if (isXlsxExcelFile(templateFile.getAbsolutePath())) {
            excelType = ExcelTypeEnum.XLSX;
        }
        ExcelWriter writer = EasyExcel.write(targetFile).withTemplate(templateFile).excelType(excelType).build();
        Workbook workbook = writer.writeContext().writeWorkbookHolder().getWorkbook();
        // 必须设置强制计算公式：不然公式会以字符串的形式显示在excel中，而不进行相关的运算
        workbook.setForceFormulaRecalculation(true);
        return writer;
    }

}
