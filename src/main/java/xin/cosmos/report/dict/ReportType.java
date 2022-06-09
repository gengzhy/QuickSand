package xin.cosmos.report.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xin.cosmos.basic.dict.IDict;

/**
 * 导出报表类型 - 枚举字典
 */
@Getter
@AllArgsConstructor
public enum ReportType implements IDict<ReportType> {
    G01("G01", "G01资产负债项目统计表", G01Sheets.values()),
    ;
    private final String table;
    private final String desc;
    private final IDict<G01Sheets>[] sheets;

    /**
     * G01资产负债项目统计表 - 页签
     */
    @Getter
    @AllArgsConstructor
    public enum G01Sheets implements IDict<G01Sheets> {
        G0100(ReportType.G01, "G0100", "G01资产负债项目统计表"),
        ;
        private final ReportType parent;
        private final String sheetName;
        private final String desc;
    }
}
