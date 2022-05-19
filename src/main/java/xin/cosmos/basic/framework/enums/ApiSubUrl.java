package xin.cosmos.basic.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 第三方服务接口-二级接口枚举
 *
 */
@Getter
@AllArgsConstructor
public enum ApiSubUrl {
    /**
     * 上海票交所票据承兑人信息披露查询 - 根据票据承兑人名称查询票据承兑人信息列表
     */
    SHCPE_DISCLOSURE_FINDACCEPTNAME(ApiRootUrl.SHCPE_DISCLOSURE, "findAccInfoListByAcptName", "根据票据承兑人名称查询票据承兑人信息列表"),
    /**
     * 上海票交所票据承兑人信息披露查询 - 根据票据承兑人名称查询票据承兑信用信息披露
     */
    SHCPE_DISCLOSURE_FINDSETTLEPAGE(ApiRootUrl.SHCPE_DISCLOSURE, "findAccInfoListByAcptName", "根据票据承兑人名称查询票据承兑信用信息披露"),

    ;
    private final ApiRootUrl rootUrl;
    private final String api;
    private final String desc;
}
