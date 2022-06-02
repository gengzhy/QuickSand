package xin.cosmos.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.api.param.FindSettlePageParam;
import xin.cosmos.basic.api.vo.AccInfoListByAcptNameVO;
import xin.cosmos.basic.api.vo.FindSettlePageVO;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.define.SingleParam;
import xin.cosmos.basic.dict.bill.disclosure.BillAcceptanceMetaType;
import xin.cosmos.basic.easyexcel.helper.EasyExcelHelper;
import xin.cosmos.dto.BillAcceptanceDisclosureDataExcelDownloadDTO;
import xin.cosmos.dto.BillAcceptanceMeta;
import xin.cosmos.entity.BillAcceptanceExcelDownloadParam;
import xin.cosmos.service.BillAcceptanceApiService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author geng
 */
@Api(tags = "票据承兑人信息查询-Controller")
@Controller
@RequestMapping(value = "bill/disclosure")
public class BillAcceptanceController {
    @Autowired
    private BillAcceptanceApiService billAcceptanceApiService;

    @ApiOperation(value = "票据承兑人信用披露信息-元数据类类型列表")
    @PostMapping(value = "business/types/get")
    @ResponseBody
    public ResultVO<List<Map<String, String>>> getBusinessTypes() {
        List<Map<String, String>> businessTypes = new ArrayList<>();
        for (BillAcceptanceMetaType type : BillAcceptanceMetaType.values()) {
            businessTypes.add(new HashMap() {{
                put("name", type.getDesc());
                put("value", type.name());
            }});
        }
        return ResultVO.success(businessTypes);
    }

    @ApiOperation(value = "根据票据承兑人名称查询票据承兑人信息列表")
    @PostMapping(value = "findAccInfoListByAcptName")
    @ResponseBody
    public ResultVO<AccInfoListByAcptNameVO> findAccInfoListByAcptName(@Valid @RequestBody SingleParam<String> acceptName) {
        return billAcceptanceApiService.findAccInfoListByAcptName(acceptName);
    }

    @ApiOperation(value = "票据承兑信用信息披露查询")
    @PostMapping(value = "findSettlePage")
    @ResponseBody
    public ResultVO<FindSettlePageVO> findSettlePage(@Valid @RequestBody FindSettlePageParam param) {
        return billAcceptanceApiService.findSettlePage(param);
    }

    @ApiOperation(value = "票据承兑人元数据Excel上传")
    @ResponseBody
    @PostMapping("/upload/meta/data")
    public ResultVO<?> upload(@ApiParam(name = "file", value = "票据承兑人元数据", required = true) @RequestPart(value = "file") MultipartFile file,
                              @ApiParam(name = "busiType", value = "业务类型", required = true) @RequestParam("busiType") String busiType) {
        String msg = billAcceptanceApiService.uploadBillAcceptanceMetaData(file, busiType);
        return ResultVO.success(msg);
    }

    @ApiOperation(value = "票据承兑人信用披露信息批量下载", produces = "application/octet-stream")
    @ResponseBody
    @PostMapping("/disclosure/web/download")
    public void downloadPostBatchBillAcceptanceDisclosureDataExcelFile(@Valid @RequestBody BillAcceptanceExcelDownloadParam param, HttpServletResponse response) {
        List<BillAcceptanceDisclosureDataExcelDownloadDTO> data = billAcceptanceApiService.queryBatchBillAcceptanceDisclosureData(param);
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssS"));
        String fileName = param.getBusiType().getDesc() + "[" + param.getShowMonth() + "]票据承兑信用信息披露查询数据" + ts;
        EasyExcelHelper.downloadExcelToResponse(response, fileName, data, BillAcceptanceDisclosureDataExcelDownloadDTO.class);
    }

    @ApiOperation(value = "票据承兑人元数据Excel模板下载", produces = "application/octet-stream")
    @ResponseBody
    @PostMapping("/download/template")
    public void downloadExcelFile(HttpServletResponse response) {
        String fileName = "票据承兑人元数据模板v" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssS"));
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("template/bill_source_data_template.xlsx");
        List<BillAcceptanceMeta> data = EasyExcelHelper.doReadExcelData(inputStream, BillAcceptanceMeta.class);
        EasyExcelHelper.downloadExcelToResponse(response, fileName, data, BillAcceptanceMeta.class);
    }
}
