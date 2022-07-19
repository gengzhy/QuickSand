package xin.cosmos.basic.ocr.baidu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.auth.DevAuth;
import com.baidu.aip.util.AipClientConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.ocr.baidu.dict.IDCardKeywords;
import xin.cosmos.basic.ocr.baidu.dict.LicenseKeywords;
import xin.cosmos.basic.ocr.dto.BusinessLicense;
import xin.cosmos.basic.ocr.dto.IdCard;
import xin.cosmos.basic.util.FileUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 百度OCR识别操作类
 */
@Slf4j
public class BaiduOcrHandler {

    /**
     * 身份证正面OCR识别
     *
     * @param idCardFrontImage 身份证正面照片（支持：{@linkplain File}及{@linkplain MultipartFile}）
     * @return IdCard
     */
    public static IdCard handleFrontIdCard(Object idCardFrontImage) throws Exception {
        String accessToken = getAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            throw new PlatformException("获取token失败");
        }
        String base64ImgParam = getImageBase64Param(idCardFrontImage);
        String param = "id_card_side=front&image=" + base64ImgParam;
        String jsonStr = HttpUtil.post(BaiduOcrConstant.ID_CARD_FRONT_URL, accessToken, param);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        log.info("身份证正面OCR识别结果=>{}", jsonObject);

        // 解析身份证正面数据
        String status = jsonObject.getString("image_status");
        if (!"normal".equals(status)) {
            throw new PlatformException("身份证图片识别失败:%s", status);
        }
        JSONObject wordsResult = jsonObject.getJSONObject("words_result");
        if (wordsResult.isEmpty()) {
            throw new PlatformException("没有解析到任何数据");
        }
        IdCard idCard = new IdCard();
        buildIdCard(idCard, wordsResult);
        return idCard;
    }

    /**
     * 身份证正反面OCR识别
     *
     * @param idCardFrontImage 身份证正面照片（支持：{@linkplain File}及{@linkplain MultipartFile}）
     * @return IdCard
     */
    public static IdCard handleMultiIdCard(Object idCardFrontImage) throws Exception {
        String accessToken = getAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            throw new PlatformException("获取token失败");
        }
        String base64ImgParam = getImageBase64Param(idCardFrontImage);
        String param = "image=" + base64ImgParam;
        String jsonStr = HttpUtil.post(BaiduOcrConstant.ID_CARD_MULTI_URL, accessToken, param);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        log.info("身份证正反面OCR识别结果=>{}", jsonObject);

        // 解析正反面
        JSONArray wordsResult = jsonObject.getJSONArray("words_result");
        if (wordsResult.isEmpty()) {
            throw new PlatformException("没有解析到任何数据");
        }
        IdCard idCard = new IdCard();
        for (Object o : wordsResult) {
            JSONObject obj = (JSONObject) o;
            JSONObject cardResult = obj.getJSONObject("card_result");
            if (cardResult == null) {
                continue;
            }
            buildIdCard(idCard, cardResult);
        }
        return idCard;
    }

    /**
     * 营业执照OCR识别
     *
     * @param businessLicenseImage 营业执照照片（支持：{@linkplain File}及{@linkplain MultipartFile}）
     * @return IdCard
     */
    public static BusinessLicense handleBusinessLicense(Object businessLicenseImage) throws Exception {
        String accessToken = getAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            throw new PlatformException("获取token失败");
        }
        String base64ImgParam = getImageBase64Param(businessLicenseImage);
        String param = "detect_direction=true&" + "image=" + base64ImgParam;
        String jsonStr = HttpUtil.post(BaiduOcrConstant.BUSINESS_LICENSE_URL, accessToken, param);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        log.info("营业执照OCR识别结果=>{}", jsonObject);
        if (!jsonObject.containsKey("words_result")) {
            throw new PlatformException("识别营业执照接口返回信息状态失败");
        }
        BusinessLicense license = new BusinessLicense();
        JSONObject wordsResult = jsonObject.getJSONObject("words_result");
        buildBusinessLicense(license, wordsResult);
        return license;
    }

    /**
     * 构建身份证信息实体对象
     *
     * @param idCard 身份证对象
     * @param meta   接口返回身份证元数据
     */
    private static void buildIdCard(IdCard idCard, JSONObject meta) {
        for (Map.Entry<String, Object> entry : meta.entrySet()) {
            String key = entry.getKey();
            String resultStr = entry.getValue().toString();
            JSONObject result = JSON.parseObject(resultStr);
            String value = result.getString("words");
            switch (key) {
                case IDCardKeywords.FRONT_NAME:
                    idCard.setName(value);
                    break;
                case IDCardKeywords.FRONT_SEX:
                    idCard.setSex(value);
                    break;
                case IDCardKeywords.FRONT_NATION:
                    idCard.setNation(value);
                    break;
                case IDCardKeywords.FRONT_BIRTH:
                    idCard.setBirthDate(value);
                    break;
                case IDCardKeywords.FRONT_ADDRESS:
                    idCard.setAddress(value);
                    break;
                case IDCardKeywords.FRONT_ID:
                    idCard.setCardNumber(value);
                    break;
                case IDCardKeywords.BACK_SIGN_ORG:
                    idCard.setSignOrg(value);
                    break;
                case IDCardKeywords.BACK_SIGN_DATE:
                    idCard.setSignDate(value);
                    break;
                case IDCardKeywords.BACK_EXPIRE:
                    idCard.setExpiredDate(value);
                    break;
                default:
            }
        }
    }

    /**
     * 构建营业执照信息实体对象
     *
     * @param license 营业执照对象
     * @param meta    接口返回营业执照元数据
     */
    private static void buildBusinessLicense(BusinessLicense license, JSONObject meta) {
        for (Map.Entry<String, Object> entry : meta.entrySet()) {
            String key = entry.getKey();
            String resultStr = entry.getValue().toString();
            JSONObject result = JSON.parseObject(resultStr);
            String value = result.getString("words");
            switch (key) {
                case LicenseKeywords.UNIT_NAME:
                    license.setUnitName(value);
                    break;
                case LicenseKeywords.UNIT_TYPE:
                    license.setUnitType(value);
                    break;
                case LicenseKeywords.LEGAL_PERSON:
                    license.setLegalPerson(value);
                    break;
                case LicenseKeywords.ADDRESS:
                    license.setAddress(value);
                    break;
                case LicenseKeywords.CODE:
                    license.setCode(value);
                    break;
                case LicenseKeywords.ID_NUMBER:
                    license.setIdNumber(value);
                    break;
                case LicenseKeywords.VALIDITY:
                    license.setValidity(value);
                    break;
                case LicenseKeywords.REGISTERED_CAPITAL:
                    license.setRegisteredCapital(value);
                    break;
                case LicenseKeywords.COMPANY_CREATE_DATE:
                    license.setCompanyCreateDate(value);
                    break;
                case LicenseKeywords.LAYOUT:
                    license.setLayout(value);
                    break;
                case LicenseKeywords.BUSINESS_SCOPE:
                    license.setBusinessScope(value);
                    break;
                default:
            }
        }
    }

    /**
     * 获取目标文件类型的返回结果
     *
     * @param file File或MultipartFile类型
     * @return
     * @throws Exception
     */
    private static File transferToFile(Object file) throws Exception {
        File img;
        if (file instanceof File) {
            img = (File) file;
        } else if (file instanceof MultipartFile) {
            img = FileUtils.multipartFileToFile((MultipartFile) file);
        } else {
            throw new UnsupportedOperationException("仅支持File或MultipartFile类型的参数");
        }
        return img;
    }

    /**
     * 获取接口请求参数的base64编码格式的图片参数
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static String getImageBase64Param(Object file) throws Exception {
        File img;
        if (file instanceof File) {
            img = (File) file;
        } else if (file instanceof MultipartFile) {
            img = FileUtils.multipartFileToFile((MultipartFile) file);
        } else {
            throw new UnsupportedOperationException("仅支持File或MultipartFile类型的参数");
        }
        byte[] bytes = FileUtil.readFileByBytes(img);
        String encode = Base64Util.encode(bytes);
        return URLEncoder.encode(encode, BaiduOcrConstant.CHARSET);
    }

    /**
     * 获取百度云API的access_token
     * 所需参数：grant_type - 固定值：client_credentials
     * client_id - 百度智能云注册的应用程序API Key
     * client_secret - 百度智能云注册的应用程序Secret Key
     *
     * @return access_token
     */
    public static String getAccessToken() {
        try {
            org.json.JSONObject jsonObject = DevAuth.oauth(BaiduOcrConstant.CLIENT_ID, BaiduOcrConstant.CLIENT_SECRET, config());
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 请求额外参数配置
     *
     */
    private static AipClientConfiguration config() {
        AipClientConfiguration config = new AipClientConfiguration();
        config.setConnectionTimeoutMillis(60000);
        config.setSocketTimeoutMillis(60000);
        return config;
    }

}
