package xin.cosmos.basic.api;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;
import xin.cosmos.basic.define.ResultVO;

/**
 *
 * 票据承兑人信息接口服务类
 *
 * @author geng
 */
@Api(tags = "票据承兑信息服务接口")
@Component
public interface IBillAcceptanceApiService {

    /**
     * 接口测试
     * @param param
     * @return
     */
    ResultVO<String> querySomething(String param);
}
