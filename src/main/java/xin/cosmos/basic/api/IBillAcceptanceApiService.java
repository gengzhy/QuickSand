package xin.cosmos.basic.api;

import org.springframework.stereotype.Component;
import xin.cosmos.basic.api.param.AccInfoListByAcptNameParam;
import xin.cosmos.basic.api.vo.AccInfoListByAcptNameVO;
import xin.cosmos.basic.framework.annotation.ApiService;
import xin.cosmos.basic.framework.annotation.ApiServiceOperation;
import xin.cosmos.basic.framework.enums.ApiRootUrl;
import xin.cosmos.basic.framework.enums.ApiSubUrl;

/**
 * 票据承兑人信息接口服务类
 * @Component 可以可无
 *
 * @author geng
 */
@ApiService(ApiRootUrl.SHCPE_DISCLOSURE)
@Component
public interface IBillAcceptanceApiService {

    /**
     * 根据票据承兑人名称查询票据承兑人信息列表
     *
     * @param param 请求参数
     * @return
     */
    @ApiServiceOperation(ApiSubUrl.SHCPE_DISCLOSURE_FINDACCEPTNAME)
    AccInfoListByAcptNameVO findAccInfoListByAcptName(AccInfoListByAcptNameParam param);
}
