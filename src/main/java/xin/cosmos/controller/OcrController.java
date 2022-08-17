package xin.cosmos.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.ocr.baidu.BaiduOcr;
import xin.cosmos.basic.ocr.baidu.FileUtil;
import xin.cosmos.basic.ocr.baidu.parser.BusinessLicenseParser;
import xin.cosmos.basic.ocr.baidu.parser.IDCardParser;
import xin.cosmos.basic.ocr.model.BusinessLicense;
import xin.cosmos.basic.ocr.model.IdCard;
import xin.cosmos.basic.util.FileUtils;

/**
 * 调用百度的文字识别Api
 */
@Api(tags = "OCR识别")
@Slf4j
@RestController
@RequestMapping(value = "/ocr")
public class OcrController {

    @ApiOperation(value = "身份证正面图片识别")
    @PostMapping("/idCard/front")
    public ResultVO<IdCard> idCardFront(@ApiParam(name = "file", value = "身份证正面图片", required = true) @RequestPart(value = "file") MultipartFile file) throws Exception {
        return ResultVO.success(new IDCardParser().parse(BaiduOcr.create().idCardFront(FileUtil.readFileByBytes(FileUtils.transferToFile(file)))));
    }

    @ApiOperation(value = "身份证图片识别")
    @PostMapping("/idCard")
    public ResultVO<IdCard> idCard(@ApiParam(name = "file", value = "身份证图片", required = true) @RequestPart(value = "file") MultipartFile file) throws Exception {
        return ResultVO.success(new IDCardParser().parse(BaiduOcr.create().idCardMulti(FileUtil.readFileByBytes(FileUtils.transferToFile(file)))));
    }

    @ApiOperation(value = "营业执照图片识别")
    @PostMapping("/businessLicense")
    public ResultVO<BusinessLicense> businessLicense(@ApiParam(name = "file", value = "营业执照图片", required = true) @RequestPart(value = "file") MultipartFile file) throws Exception {
        return ResultVO.success(new BusinessLicenseParser().parse(BaiduOcr.create().businessLicense(FileUtil.readFileByBytes(FileUtils.transferToFile(file)))));
    }

}

