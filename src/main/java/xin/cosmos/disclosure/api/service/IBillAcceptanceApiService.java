package xin.cosmos.disclosure.api.service;

import xin.cosmos.basic.framework.annotation.ApiService;
import xin.cosmos.basic.framework.annotation.ApiSupport;
import xin.cosmos.basic.framework.enums.ApiRootUrl;
import xin.cosmos.basic.framework.enums.ApiSubUrl;
import xin.cosmos.basic.framework.header.DynamicHeaders;
import xin.cosmos.disclosure.api.param.AccInfoListByAcptNameParam;
import xin.cosmos.disclosure.api.param.FindSettlePageParam;
import xin.cosmos.disclosure.api.vo.AccInfoListByAcptNameVO;
import xin.cosmos.disclosure.api.vo.FindSettlePageVO;

/**
 * 票据承兑人信息接口服务类
 * @Component 可以可无
 *
 * @author geng
 */
@ApiSupport(ApiRootUrl.SHCPE_DISCLOSURE)
public interface IBillAcceptanceApiService {

    /**
     * 根据票据承兑人名称查询票据承兑人信息列表
     *
     * @param param 请求参数
     * @return
     */
    @ApiService(value = ApiSubUrl.SHCPE_DISCLOSURE_FINDACCEPTNAME, headers = DynamicHeaders.SHCPE_DISCLOSURE)
    AccInfoListByAcptNameVO findAccInfoListByAcptName(AccInfoListByAcptNameParam param);

    /**
     * 根据票据承兑人名称查询票据承兑人信息列表
     *
     * @param param 请求参数
     * @return
     */
    @ApiService(value = ApiSubUrl.SHCPE_DISCLOSURE_FINDSETTLEPAGE, headers = DynamicHeaders.SHCPE_DISCLOSURE)
    FindSettlePageVO findSettlePage(FindSettlePageParam param);
}
