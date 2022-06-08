package xin.cosmos.report.dict;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import xin.cosmos.basic.dict.IDict;

/**
 * 元数据报表类型
 */
@Getter
@AllArgsConstructor
public enum MetaReportType implements IDict<MetaReportType> {
    G01("G01", "G01资产负债项目统计表", G01Sheets.values()),
    ;
    private final String code;
    private final String desc;
    private final IDict<G01Sheets>[] sheets;

    @Getter
    @AllArgsConstructor
    @ApiModel(description = "G01资产负债项目统计表 - 页签")
    public static enum G01Sheets implements IDict<G01Sheets> {
        G0100("G0100", "G01资产负债项目统计表"),
        ;
        private final String code;
        private final String desc;
    }
}
