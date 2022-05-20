package xin.cosmos.basic.dict.bill.disclosure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xin.cosmos.basic.dict.IDict;

/**
 * 票据承兑人信用披露信息下载类型
 */
@Getter
@AllArgsConstructor
public enum BillAcceptanceBusiType implements IDict<BillAcceptanceBusiType> {
    META_GUIZHOU_TOP100("贵州省企业100强"),
    META_BANK_CREDIT_RMB5000W("全行授信5000万元以上客户")
    ;
    private final String desc;

    @Override
    public String getDesc() {
        return this.desc;
    }

}
