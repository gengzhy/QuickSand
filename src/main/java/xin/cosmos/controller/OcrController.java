package xin.cosmos.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.httpclient.HttpClient;
import xin.cosmos.basic.ocr.baidu.Base64Util;
import xin.cosmos.basic.ocr.baidu.FileUtil;
import xin.cosmos.basic.ocr.baidu.HttpUtil;
import xin.cosmos.basic.ocr.dto.BusinessLicense;
import xin.cosmos.basic.ocr.dto.IdCard;
import xin.cosmos.basic.util.FileUtils;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 调用百度的文字识别Api
 */
@Api(tags = "OCR识别")
@Slf4j
@RestController
@RequestMapping(value = "/ocr")
public class OcrController {
    // API key（注册应用获取）
    private final String client_id = "Yg0hlhXTw9sgGhUg69H9kbfa";

    // Secret Key（注册应用获取）
    private final String client_secret = "grGV4tTQkq01GZCnAEuEWBFP5QgjDRAC";

    @Value("${idcardUrl:https://aip.baidubce.com/rest/2.0/ocr/v1/idcard}")
    private String idcardUrl;

    @Value("${businessLicenseUrl:https://aip.baidubce.com/rest/2.0/ocr/v1/business_license}")
    private String businessLicenseUrl;

    @Value("${accessTokenUrl:https://aip.baidubce.com/oauth/2.0/token}")
    private String accessTokenUrl;

    private final SimpleDateFormat idcardDateFormat = new SimpleDateFormat("yyyyMMdd");
    private final SimpleDateFormat businessDateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 识别身份证文字
     *
     *
     *
     * @return
     */
    @ApiOperation(value = "身份证号识别")
    @PostMapping("/idcard")
    public ResultVO<List<IdCard>> idCard(@ApiParam(name = "files", value = "身份证图片", required = true) @RequestPart(value = "files") MultipartFile files) {
        List<IdCard> list = new ArrayList<>();
        try {
            if (files != null /*&& files.length > 0*/) {
//                for (int j = 0; j < files.length; j++) {
                    String address = "", cardNumber = "", name = "", sex = "", nation = "", birthDate = "";
                    String accessToken = getAccessToken();
                    if (StringUtils.isEmpty(accessToken)) {
                        log.info("获取Token失败!");
//                        continue;
                    }
//                    File file =  FileUtils.transferToFile(files[j]);//MultipartFile 转换为File
//                    File file =  FileUtils.transferToFile(files);//MultipartFile 转换为File
                    File file =  FileUtils.multipartFileToFile(files);//MultipartFile 转换为File
                    byte[] bytes = FileUtil.readFileByBytes(file);
                    String encode = Base64Util.encode(bytes);
                    String imgParam = URLEncoder.encode(encode, "utf-8");
                    String param = "id_card_side=front&image=" + imgParam;
                    String jsonStr = HttpUtil.post(idcardUrl, accessToken, param);
                    JSONObject jsonObject = JSON.parseObject(jsonStr);
                    String status = jsonObject.getString("image_status");
                    if (!status.equals("normal")) {
                        log.error("识别身份信息接口返回状态失败！");
//                        continue;
                    }
                    JSONObject words_resultList = jsonObject.getJSONObject("words_result");
                    for (Map.Entry<String, Object> entry : words_resultList.entrySet()) {
                        String key = entry.getKey();
                        String resultStr = entry.getValue().toString();
                        JSONObject result = JSON.parseObject(resultStr);
                        if (key.equals("住址")) {
                            address = result.getString("words");
                        } else if (key.equals("公民身份号码")) {
                            cardNumber = result.getString("words");
                        } else if (key.equals("出生")) {
                            birthDate = result.getString("words");
                        } else if (key.equals("姓名")) {
                            name = result.getString("words");
                        } else if (key.equals("性别")) {
                            sex = result.getString("words");
                        } else if (key.equals("民族")) {
                            nation = result.getString("words");
                        }
                    }
                    IdCard idCard = new IdCard();
                    idCard.setAddress(address);
                    idCard.setBirthDate(birthDate);
                    idCard.setCardNumber(cardNumber);
                    idCard.setName(name);
                    idCard.setSex(sex);
                    idCard.setNation(nation);
                    list.add(idCard);
//                }
            }
            log.error("上传身份证文件不能为空!");
            return ResultVO.failed("上传身份证文件不能为空!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ResultVO.failed("识别身份信息失败!");
        }
        if (list.size() <= 0) {
            return ResultVO.failed("识别身份信息失败!");
        }
        log.info("识别结果：{}", list);
        return ResultVO.success(list);
    }

    /**
     * 识别营业执照
     *
     * @return
     */
    @ApiOperation(value = "营业执照识别")
    @PostMapping("/businessLicense")
    public ResultVO<List<BusinessLicense>> businessLicense(@RequestParam("files") MultipartFile[] files) {
        List<BusinessLicense> list = new ArrayList<>();
        try {
            if (files != null && files.length > 0) {
                for (int j = 0; j < files.length; j++) {
                    String unitName = "", unitType = "", legalPerson = "", address = "", validity = "", idNumber = "",
                            code = "", businessScope = "", layout = "", registeredCapital = "", companyCreateDate = "";
                    String accessToken = getAccessToken();
                    if (StringUtils.isEmpty(accessToken)) {
                        log.info("获取Token失败!");
                        continue;
                    }
                    File file = FileUtils.transferToFile(files[j]);//MultipartFile 转换为File
                    byte[] bytes = FileUtil.readFileByBytes(file);
                    String encode = Base64Util.encode(bytes);
                    String imgParam = URLEncoder.encode(encode, "utf-8");
                    String param = "detect_direction=true&" + "image=" + imgParam;
                    String jsonStr = HttpUtil.post(businessLicenseUrl, accessToken, param);
                    JSONObject jsonObject = JSON.parseObject(jsonStr);
                    if (!jsonObject.containsKey("words_result")) {
                        log.info("识别营业执照接口返回信息状态失败！");
                        continue;
                    }
                    JSONObject words_resultList = jsonObject.getJSONObject("words_result");
                    for (Map.Entry<String, Object> entry : words_resultList.entrySet()) {
                        String key = entry.getKey();
                        String resultStr = entry.getValue().toString();
                        JSONObject result = JSON.parseObject(resultStr);
                        if (key.equals("单位名称")) {
                            unitName = result.getString("words");
                        } else if (key.equals("类型")) {
                            unitType = result.getString("words");
                        } else if (key.equals("法人")) {
                            legalPerson = result.getString("words");
                        } else if (key.equals("地址")) {
                            address = result.getString("words");
                        } else if (key.equals("有效期")) {
                            validity = result.getString("words");
                        } else if (key.equals("证件编号")) {
                            idNumber = result.getString("words");
                        } else if (key.equals("社会信用代码")) {
                            code = result.getString("words");
                        } else if (key.equals("经营范围")) {
                            businessScope = result.getString("words");
                        } else if (key.equals("组成形式")) {
                            layout = result.getString("words");
                        } else if (key.equals("成立日期")) {
                            companyCreateDate = result.getString("words");
                        } else if (key.equals("注册资本")) {
                            registeredCapital = result.getString("words");
                        }
                    }
                    BusinessLicense license = new BusinessLicense();
                    license.setAddress(address);
                    license.setCode(code);
                    license.setIdNumber(idNumber);
                    license.setLegalPerson(legalPerson);
                    license.setUnitName(unitName);
                    license.setUnitType(unitType);
                    license.setValidity(validity);
                    license.setRegisteredCapital(registeredCapital);
                    license.setCompanyCreateDate(companyCreateDate);
                    license.setLayout(layout);
                    license.setBusinessScope(businessScope);
                    list.add(license);
                }
            } else {
                log.info("上传营业执照文件不能为空!");
                return ResultVO.failed("上传营业执照文件不能为空!");
            }
        } catch (
                Exception e) {
            log.error(e.getMessage(), e);
            return ResultVO.failed("识别营业执照信息失败!");
        }
        if (list.isEmpty()) {
            return ResultVO.failed("识别营业执照信息失败!");
        }
        log.info("识别结果：{}", list);
        return ResultVO.success(list);
    }


    /**
     * 获取百度云API的access_token
     *
     * @return access_token
     */
    private String getAccessToken() {
        //参数：?grant_type=client_credentials&client_id=【官网获取的AK】&client_secret=【官网获取的SK】'
        Map<String, String> params = new HashMap<String, String>() {{
            put("grant_type", "client_credentials");
            put("client_id", client_id);
            put("client_secret", client_secret);
        }};
        String access_token = "";
        try {
            String result = HttpClient.create().get(accessTokenUrl, params);
            JSONObject jsonObject = JSON.parseObject(result);
            access_token = jsonObject.getString("access_token");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return access_token;
    }

}

