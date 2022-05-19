package xin.cosmos.service;

import io.netty.util.internal.ObjectUtil;
import org.springframework.stereotype.Service;
import xin.cosmos.basic.api.IBillAcceptanceApiService;
import xin.cosmos.basic.api.param.AccInfoListByAcptNameParam;
import xin.cosmos.basic.api.vo.AccInfoListByAcptNameVO;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.util.ObjectsUtil;

/**
 * 票据承兑人信息接口服务类
 *
 * @author geng
 */
@Service
public class BillAcceptanceApiService {

    private IBillAcceptanceApiService billAcceptanceApiService;

    public BillAcceptanceApiService(IBillAcceptanceApiService billAcceptanceApiService) {
        this.billAcceptanceApiService = billAcceptanceApiService;
    }

    /**
     * 根据票据承兑人名称查询票据承兑人信息列表
     *
     * @param acceptName 票据承兑人名称
     * @return
     */
    public ResultVO<AccInfoListByAcptNameVO> findAccInfoListByAcptName(String acceptName) {
        AccInfoListByAcptNameParam param = new AccInfoListByAcptNameParam();
        param.setAcceptanceName(acceptName);
        param.setRandom(ObjectsUtil.randomNumber(5));
        return ResultVO.success(billAcceptanceApiService.findAccInfoListByAcptName(param));
    }
}
