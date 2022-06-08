package xin.cosmos.disclosure.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xin.cosmos.basic.dict.IDict;

/**
 * 票据承兑人信用披露信息-元数据类型
 * @author geng
 */
@Getter
@AllArgsConstructor
public enum BillAcceptanceMetaType implements IDict<BillAcceptanceMetaType> {
    GUIZHOU_TOP100("贵州省企业100强"),
    GUIZHOU_TOP100_SHAREHOLDER("贵州省企业100强的股东"),
    GUIZHOU_TOP100_SUBSIDIARY("贵州省企业100强的子公司"),

    CREDIT_RMB5000W("5000万元以上授信客户"),
    CREDIT_RMB5000W_SHAREHOLDER("5000万元以上授信客户股东"),
    CREDIT_RMB5000W_SUBSIDIARY("5000万元以上授信客户子公司"),

    ;
    private final String desc;

    @Override
    public String getDesc() {
        return this.desc;
    }

}
