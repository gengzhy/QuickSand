package xin.cosmos.basic.generator;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import xin.cosmos.basic.generator.model.ModelProperty;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Excel标题行单元格转换为Java实体类属性信息
 */
public class ExcelHeadRowToPropertiesGenerator {
    /**
     * Excel标题行转换为Java实体所需的属性信息集合
     *
     * @param excelFile      excel文件
     * @param sheetNo        页签（下标从0开始）
     * @param headRowNumber  标题行（如1，代表一行标题；2代表2行标题）
     * @param endColumnIndex 标题结束列下标（下标从0开始）
     * @return
     */
    public static List<ModelProperty> generate(File excelFile, int sheetNo, int headRowNumber, int endColumnIndex) {
        // 最终返回的属性信息集合
        List<ModelProperty> propertyList = new LinkedList<>();
        EasyExcel.read(excelFile, new ExcelHeadAnalysisEventListener(propertyList, endColumnIndex)).sheet(sheetNo).autoTrim(true).headRowNumber(headRowNumber).doRead();
        return propertyList;
    }

    /**
     * Excel标题行转换为Java实体所需的属性信息集合
     *
     * @param excelFileInputStream excel文件流
     * @param sheetNo              页签（下标从0开始）
     * @param headRowNumber        标题行（如1，代表一行标题；2代表2行标题）
     * @param endColumnIndex       标题结束列下标（下标从0开始）
     * @return
     */
    public static List<ModelProperty> generate(InputStream excelFileInputStream, int sheetNo, int headRowNumber, int endColumnIndex) {
        // 最终返回的属性信息集合
        List<ModelProperty> propertyList = new LinkedList<>();
        EasyExcel.read(excelFileInputStream, new ExcelHeadAnalysisEventListener(propertyList, endColumnIndex)).sheet(sheetNo).headRowNumber(headRowNumber).doRead();
        return propertyList;
    }

    /**
     * Excel标题解析器
     */
    static class ExcelHeadAnalysisEventListener extends AnalysisEventListener<LinkedHashMap<Integer, Object>> {
        // 最终返回的属性信息集合
        List<ModelProperty> propertyList;
        // 标题结束列下标（下标从0开始）
        int endColumnIndex;

        // 标题行数
        int headRowEndIndex;
        // 记录遍历的标题行下标（从1）
        AtomicInteger index = new AtomicInteger(1);
        // 缓存标题行
        Map<Integer, ReadCellData<?>> heads = new LinkedHashMap<>();
        // 暂存model属性
        Map<Integer, ModelProperty> propertiesMap = new LinkedHashMap<>();

        /**
         * @param propertyList   最终返回的属性信息集合
         * @param endColumnIndex 标题结束列下标（下标从0开始）
         */
        public ExcelHeadAnalysisEventListener(List<ModelProperty> propertyList, int endColumnIndex) {
            this.propertyList = propertyList;
            this.endColumnIndex = endColumnIndex;
        }

        @Override
        public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
            headRowEndIndex = context.readSheetHolder().getHeadRowNumber();
            if (index.getAndAdd(1) == headRowEndIndex) {
                headMap.forEach((k, v) -> {
                    if (k <= endColumnIndex) {
                        heads.put(k, v);
                        String name = "c_" + k;
                        String remark = v.getStringValue();
                        propertiesMap.put(k, ModelProperty.builder().index(k).name(name).remark(remark).build());
                    }
                });
            }
        }

        @Override
        public void invoke(LinkedHashMap<Integer, Object> data, AnalysisContext context) {
            Integer rowIndex = context.readRowHolder().getRowIndex();
            // invoke方法仅需要读取一行数据行，作为数据类型判断。不在进行往下处理
            if (rowIndex > index.decrementAndGet()) {
                return;
            }
            ReadHolder holder = context.currentReadHolder();
            // 如果不满足以下条件，全部数据类型默认为String
            if (!(holder instanceof ReadSheetHolder)) {
                return;
            }
            ReadSheetHolder sheetHolder = (ReadSheetHolder) holder;
            Map<Integer, Cell> cellMap = sheetHolder.getCellMap();
            heads.forEach((index, v) -> {
                ReadCellData<?> cell = (ReadCellData<?>) cellMap.get(index);
                ModelProperty modelProperty = propertiesMap.get(index);
                if (cell == null || modelProperty == null) {
                    return;
                }
                switch (cell.getType()) { // 日期
                    case DATE:
                        System.out.print(cell.getStringValue() + ",");
                        modelProperty.setType(FieldDataType.DATE.name());
                        break;
                    // 数字（此处较为复杂，拿不到具体的数据类型，只能取几个特殊的，其他的当做Number来处理）
                    case NUMBER:
                        // 时间格式
                        DataFormatData formatData = cell.getDataFormatData();
                        if (formatData == null) {
                            modelProperty.setType(FieldDataType.NUMBER.name());
                            break;
                        }
                        String format = formatData.getFormat();
                        // 百分数
                        if (format.endsWith("%")) {
                            modelProperty.setType(FieldDataType.PERCENT.name());
                        }
                        // 日期
                        else if (format.startsWith("yyyy") || format.startsWith("YYYY")) {
                            modelProperty.setType(FieldDataType.DATE.name());
                        } else {
                            modelProperty.setType(FieldDataType.NUMBER.name());
                        }
                        break;
                    // 布尔值
                    case BOOLEAN:
                        modelProperty.setType(FieldDataType.BOOLEAN.name());
                        break;
                    default:
                        modelProperty.setType(FieldDataType.STRING.name());
                }
            });
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            propertiesMap.forEach((k, v) -> {
                if (v.getType() == null) {
                    v.setType(FieldDataType.STRING.name());
                }
            });
            propertyList.addAll(propertiesMap.values());
        }
    }
}
