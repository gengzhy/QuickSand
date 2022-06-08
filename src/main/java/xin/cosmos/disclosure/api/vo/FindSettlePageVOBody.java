package xin.cosmos.disclosure.api.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "票据承兑信用信息披露记录 - 数据体")
@Data
public class FindSettlePageVOBody {
    @ApiModelProperty("披露主体基本信息")
    @JSONField(name = "entListInfoVo")
    private FindSettlePageVOBaseInfo baseInfo;

    @ApiModelProperty("披露信息数据体")
    @JSONField(name = "batchSettleInfoPage")
    private FindSettlePageVODetailInfo detailInfo;

}