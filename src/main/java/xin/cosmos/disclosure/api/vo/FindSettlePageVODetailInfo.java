package xin.cosmos.disclosure.api.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xin.cosmos.basic.codec.ApiDictDeserializer;
import xin.cosmos.disclosure.api.dict.ShowStatus;

import java.util.List;

@ApiModel(description = "票据承兑信用信息披露记录 - 披露信息数据体")
@Data
public class FindSettlePageVODetailInfo {
    @ApiModelProperty("总共数据条数")
    @JSONField(name = "total")
    private int total;

    @ApiModelProperty("每页数据大小")
    @JSONField(name = "size")
    private int size;

    @ApiModelProperty("披露信息数据体 - 详细记录")
    @JSONField(name = "records")
    private List<DetailInfoRecord> records;

    @ApiModel(description = "披露信息数据体 - 详细记录")
    @Data
    public static class DetailInfoRecord {
        @ApiModelProperty("承兑人名称")
        @JSONField(name = "acptName")
        private String acptName;

        @ApiModelProperty("承兑人开户机构名称")
        @JSONField(name = "dimAcptBranchName")
        private String dimAcptBranchName;

        @ApiModelProperty("累计承兑发生额（元）")
        @JSONField(name = "acptAmount")
        private String acptAmount;

        @ApiModelProperty("承兑余额（元）")
        @JSONField(name = "acptOver")
        private String acptOver;

        @ApiModelProperty("累计逾期发生额（元）")
        @JSONField(name = "totalOverdueAmount")
        private String totalOverdueAmount;

        @ApiModelProperty("逾期余额（元）")
        @JSONField(name = "overdueOver")
        private String overdueOver;

        @ApiModelProperty("披露信息时点日期")
        @JSONField(name = "showDate")
        private String showDate;

        @ApiModelProperty("披露日期")
        @JSONField(name = "relDate")
        private String relDate;

        @ApiModelProperty("系统备注")
        @JSONField(name = "remark")
        private String remark;

        @ApiModelProperty("企业备注")
        @JSONField(name = "entRemark")
        private String entRemark;

        @ApiModelProperty("企业详细备注")
        @JSONField(name = "entDetailRemark")
        private String entDetailRemark;

        @ApiModelProperty("披露状态")
        @JSONField(name = "showStatus", deserializeUsing = ApiDictDeserializer.class)
        private ShowStatus showStatus;

    }
}