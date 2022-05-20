package xin.cosmos.basic.api.dict.bill.disclosure;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import xin.cosmos.basic.api.dict.ApiDict;

@ApiModel(description = "票据承兑信用信息披露查询-承兑人机构类别")
@AllArgsConstructor
@Getter
public enum OrgType implements ApiDict {
    CORP("1", "企业"),
    FINANCE_CORP("2", "财务公司"),
    OTHER("-1", "其他"),
    ;

    private final String code;
    private final String desc;
}
