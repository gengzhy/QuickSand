package xin.cosmos.basic.api.dict.bill.disclosure;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import xin.cosmos.basic.api.dict.ApiDict;

@ApiModel(description = "票据承兑信用信息披露查询-披露状态")
@AllArgsConstructor
@Getter
public enum ShowStatus implements ApiDict {
    SHOWED("1", "已披露"),
    NO_SHOW("2", "未披露"),
    ;

    private final String code;
    private final String desc;

}
