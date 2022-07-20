package xin.cosmos.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.easyexcel.helper.EasyExcelHelper;
import xin.cosmos.basic.ocr.baidu.BaiduOcrHandler;
import xin.cosmos.basic.ocr.dto.BusinessLicense;
import xin.cosmos.basic.ocr.dto.IdCard;
import xin.cosmos.test.entity.UserRoleRelationship;
import xin.cosmos.test.model.MerchantMetaData;
import xin.cosmos.test.service.UserRoleRelationshipService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Api(tags = "测试-Test")
@RequestMapping(value = "test")
@Controller
public class TestController {

    @Autowired
    private UserRoleRelationshipService userRoleRelationshipService;

    @ResponseBody
    @ApiOperation(value = "事务测试")
    @PostMapping(value = "testTx")
    public ResultVO<UserRoleRelationship> testTx(@RequestParam(value = "userId") Long userId, @RequestParam(value = "roleId")Long roleId) {
        return ResultVO.success(userRoleRelationshipService.testTx(userId, roleId));
    }

    /**
     * 使用swagger进行文件上传时，需使用{@linkplain ApiParam}注解指定：name指定接收的参数名称
     * @param file 商户元数据excel文件
     */
    @ApiOperation(value = "商户证件数据上传及OCR处理")
    @ResponseBody
    @PostMapping("/handle/merchant/ocr")
    public ResultVO<?> handleOcrMerchant(@ApiParam(name = "file", value = "商户证件Excel数据", required = true) @RequestPart(value = "file") MultipartFile file) throws IOException {
        List<MerchantMetaData> dataList = EasyExcelHelper.doReadExcelData(file.getInputStream(), MerchantMetaData.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // OCR接口的QPS为10
        AtomicInteger count = new AtomicInteger(0);

        // 用于记录处理到的数据
        AtomicReference<String> handleData = new AtomicReference<>("");
        try {
            // 证件图片地址存在多个的，取最后一个
            dataList.forEach(data -> {
                // 处理身份证照片
                String idCardBackImg = data.getIdCardBackImg();
                if (StringUtils.isNotBlank(idCardBackImg)) {
                    String[] idCardBackImgArr = idCardBackImg.split(",");
                    String idCardBackImgUrl = idCardBackImgArr[idCardBackImgArr.length - 1];
                    try {
                        IdCard idCard = BaiduOcrHandler.handleMultiIdCard(idCardBackImgUrl);
                        // 如果没取到证件日期，说明身份证照片正反面放反了，需再取一次
                        if (idCard.getSignDate() == null && StringUtils.isNotBlank(data.getIdCardFrontImg())) {
                            idCardBackImgArr = data.getIdCardFrontImg().split(",");
                            idCardBackImgUrl = idCardBackImgArr[idCardBackImgArr.length - 1];
                            idCard = BaiduOcrHandler.handleMultiIdCard(idCardBackImgUrl);
                        }
                        data.setSignDate(dateFormat.format(idCard.getSignDate()));
                        data.setExpiredDate(idCard.getExpiredDate());
                    } catch (Exception e) {
                        log.error("[{}]关联的身份证[{}]OCR数据处理失败", data.getCode(), idCardBackImgUrl, e);
                    }
                }
                // 处理营业执照照片
                String licenceImg = data.getLicenceImg();
                if (StringUtils.isNotBlank(licenceImg)) {
                    String[] licenceImgArr = licenceImg.split(",");
                    String licenceImgUrl = licenceImgArr[licenceImgArr.length - 1];
                    try {
                        BusinessLicense license = BaiduOcrHandler.handleBusinessLicense(licenceImgUrl);
                        if (license.getCompanyCreateDate() != null) {
                            data.setCompanyCreateDate(dateFormat.format(license.getCompanyCreateDate()));
                        }
                        // 没值取forever（长期）
                        if (StringUtils.isEmpty(license.getValidity())
                                || "无".equals(license.getValidity())||
                                "长期".equals(license.getValidity())) {
                            data.setValidity("forever");
                        } else {
                            data.setValidity(license.getValidity());
                        }
                    } catch (Exception e) {
                        log.error("[{}]关联的营业执照[{}]OCR数据处理失败", data.getCode(), licenceImgUrl, e);
                    }
                }
                // OCR接口的QPS为10
                try {
                    if (count.incrementAndGet() % 10 == 0) {
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handleData.set(data.getCode());
            });
        } catch (Exception e) {
            log.info("OCR数据处理中断，当前成功处理到[{}]条数据", handleData.get());
        }
        log.info("OCR处理完毕，共处理数据{}条", count.get());
        String filePath = "c:/商户资质补录数据"+System.nanoTime()+".xlsx";
        EasyExcelHelper.downloadExcel(filePath, dataList, MerchantMetaData.class);
        return ResultVO.success(dataList);
    }
}
