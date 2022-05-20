package xin.cosmos.service;

import org.springframework.stereotype.Service;
import xin.cosmos.basic.api.IBillAcceptanceApiService;
import xin.cosmos.basic.api.param.AccInfoListByAcptNameParam;
import xin.cosmos.basic.api.param.FindSettlePageParam;
import xin.cosmos.basic.api.vo.AccInfoListByAcptNameVO;
import xin.cosmos.basic.api.vo.FindSettlePageVO;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.define.SingleParam;
import xin.cosmos.basic.exception.BusinessException;
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
    public ResultVO<AccInfoListByAcptNameVO> findAccInfoListByAcptName(SingleParam<String> acceptName) {
        if (ObjectsUtil.isNull(acceptName) || ObjectsUtil.isNull(acceptName.getKey())) {
            throw new BusinessException("票据承兑人名称为空");
        }
        AccInfoListByAcptNameParam param = new AccInfoListByAcptNameParam();
        param.setBillAcceptanceName(acceptName.getKey());
        param.setRandom(ObjectsUtil.randomNumber(5));
        return ResultVO.success(billAcceptanceApiService.findAccInfoListByAcptName(param));
    }

    /**
     * 票据承兑信用信息披露查询
     *
     * @param param 请求参数
     * @return
     */
    public ResultVO<FindSettlePageVO> findSettlePage(FindSettlePageParam param) {
        param.setCurrent(1);
        param.setSize(100);
        return ResultVO.success(billAcceptanceApiService.findSettlePage(param));
    }
}
