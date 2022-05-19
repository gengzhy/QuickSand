package xin.cosmos.basic.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 第三方服务接口描述枚举
 *
 * @author geng
 */
@Getter
@AllArgsConstructor
public enum ApiRootUrl {
    /**
     * 上海票交所票据承兑人信息披露查询
     */
    SHCPE_DISCLOSURE("https://disclosure.shcpe.com.cn/ent/public/", "上海票交所票据承兑人信息披露查询"),

    NOTHING(null, null)
    ;
    private final String rootUrl;
    private final String rootUrlDesc;
}
