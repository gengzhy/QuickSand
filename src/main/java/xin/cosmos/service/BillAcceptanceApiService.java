package xin.cosmos.service;

import org.springframework.stereotype.Service;
import xin.cosmos.basic.api.IBillAcceptanceApiService;
import xin.cosmos.basic.define.ResultVO;

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
     * 接口测试
     *
     * @param param
     * @return
     */
    public ResultVO<String> querySomething(String param) {
        return billAcceptanceApiService.querySomething(param);
    }
}
