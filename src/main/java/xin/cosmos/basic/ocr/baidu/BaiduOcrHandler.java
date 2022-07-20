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
import xin.cosmos.basic.httpclient.HttpClient;
import xin.cosmos.basic.ocr.baidu.dict.IDCardKeywords;
import xin.cosmos.basic.ocr.baidu.dict.LicenseKeywords;
import xin.cosmos.basic.ocr.dto.BusinessLicense;
import xin.cosmos.basic.ocr.dto.IdCard;
import xin.cosmos.basic.util.FileUtils;

import java.io.File;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度OCR识别操作类
 */
@Slf4j
public class BaiduOcrHandler {
    /**
     * 身份证日期格式化
     */
    public static ThreadLocal<DateFormat> idCardDateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));
    /**
     * 营业执照日期格式化
     */
    public static ThreadLocal<DateFormat> licenseDateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy年MM月dd日"));

    /**
     * 身份证正面OCR识别
     *
     * @param idCardFrontImage 身份证正面照片（支持：{@linkplain File}及{@linkplain MultipartFile}或者文件的字节数组bytes）
     * @return IdCard
     */
    public static IdCard handleFrontIdCard(Object idCardFrontImage) throws Exception {
        String base64ImgParam = getImageBase64Param(idCardFrontImage);
        String param = "id_card_side=front&image=" + base64ImgParam;
        return doPostFrontIdCard(param);
    }

    /**
     * 身份证正面OCR识别
     *
     * @param idCardFrontImageUrl 身份证正面照片url地址
     * @return IdCard
     */
    public static IdCard handleFrontIdCard(String idCardFrontImageUrl) throws Exception {
        String param = "id_card_side=front&url=" + idCardFrontImageUrl;
        return doPostFrontIdCard(param);
    }

    /**
     * 身份证正反面图片真正处理方法
     *
     * @param requestParams 请求参数
     */
    private static IdCard doPostFrontIdCard(String requestParams) throws Exception {
        String jsonStr = HttpUtil.post(BaiduOcrConstant.ID_CARD_FRONT_URL, getAccessToken(), requestParams);
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
     * @param idCardFrontImage 身份证正面照片（支持：{@linkplain File}及{@linkplain MultipartFile}或者文件的字节数组bytes）
     * @return IdCard
     */
    public static IdCard handleMultiIdCard(Object idCardFrontImage) throws Exception {
        String base64ImgParam = getImageBase64Param(idCardFrontImage);
        String param = "image=" + base64ImgParam;
        return doPostMultiIdCard(param);
    }

    /**
     * 身份证正反面OCR识别
     *
     * @param idCardFrontImageUrl 身份证正面照片url地址
     * @return IdCard
     */
    public static IdCard handleMultiIdCard(String idCardFrontImageUrl) throws Exception {
        String param = "url=" + idCardFrontImageUrl;
        return doPostMultiIdCard(param);
    }

    /**
     * 身份证正反面图片真正处理方法
     *
     * @param requestParams 请求参数
     */
    private static IdCard doPostMultiIdCard(String requestParams) throws Exception {
        String jsonStr = HttpUtil.post(BaiduOcrConstant.ID_CARD_MULTI_URL, getAccessToken(), requestParams);
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
     * @param businessLicenseImage 营业执照照片（支持：{@linkplain File}及{@linkplain MultipartFile}或者文件的字节数组bytes）
     * @return IdCard
     */
    public static BusinessLicense handleBusinessLicense(Object businessLicenseImage) throws Exception {
        String base64ImgParam = getImageBase64Param(businessLicenseImage);
        String param = "detect_direction=true&image=" + base64ImgParam;
        return doPostBusinessLicense(param);
    }

    /**
     * 营业执照OCR识别
     *
     * @param businessLicenseImageUrl 营业执照照片url地址
     * @return IdCard
     */
    public static BusinessLicense handleBusinessLicense(String businessLicenseImageUrl) throws Exception {
        String param = "detect_direction=true&url=" + businessLicenseImageUrl;
        return doPostBusinessLicense(param);
    }

    /**
     * 营业执照图片真正处理方法
     *
     * @param requestParams 请求参数
     */
    private static BusinessLicense doPostBusinessLicense(String requestParams) throws Exception {
        String jsonStr = HttpUtil.post(BaiduOcrConstant.BUSINESS_LICENSE_URL, getAccessToken(), requestParams);
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
    private static void buildIdCard(IdCard idCard, JSONObject meta) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
                    if (StringUtils.isNotBlank(value) && !"无".equals(value)) {
                        idCard.setBirthDate(idCardDateFormat.get().parse(value));
                    }
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
                    if (StringUtils.isNotBlank(value) && !"无".equals(value)) {
                        idCard.setSignDate(idCardDateFormat.get().parse(value));
                    }
                    break;
                case IDCardKeywords.BACK_EXPIRE:
                    if (StringUtils.isNotBlank(value) && !"无".equals(value)) {
                        if ("长期".equals(value)) {
                            idCard.setExpiredDate(value);
                        } else {
                            idCard.setExpiredDate(dateFormat.format(idCardDateFormat.get().parse(value)));
                        }
                    }
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
    private static void buildBusinessLicense(BusinessLicense license, JSONObject meta) throws ParseException {
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
                    if (StringUtils.isNotBlank(value) && !"无".equals(value)) {
                        license.setCompanyCreateDate(licenseDateFormat.get().parse(value));
                    }
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
     */
    private static String getImageBase64Param(Object file) throws Exception {
        File img;
        byte[] bytes;
        if (file instanceof File) {
            img = (File) file;
            bytes = FileUtil.readFileByBytes(img);
        } else if (file instanceof MultipartFile) {
            img = FileUtils.multipartFileToFile((MultipartFile) file);
            bytes = FileUtil.readFileByBytes(img);
        } else if (file instanceof byte[]) {
            bytes = (byte[]) file;
        } else {
            throw new UnsupportedOperationException("仅支持File或MultipartFile或byte[]类型的参数");
        }
        // 转base64
        String encode = Base64Util.encode(bytes);
        // 转url格式
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
            String accessToken = jsonObject.getString("access_token");
            if (StringUtils.isEmpty(accessToken)) {
                throw new PlatformException("获取token失败");
            }
            return accessToken;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取百度云API的access_token
     * 参数：?grant_type=client_credentials&client_id=【官网获取的AK】&client_secret=【官网获取的SK】'
     *
     * @return access_token
     */
    private String getAccessTokenOld(String accessTokenUrl, String client_id, String client_secret) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("grant_type", "client_credentials");
            put("client_id", client_id);
            put("client_secret", client_secret);
        }};
        try {
            String result = HttpClient.create().get(accessTokenUrl, params);
            JSONObject jsonObject = JSON.parseObject(result);
            String accessToken = jsonObject.getString("access_token");
            if (StringUtils.isEmpty(accessToken)) {
                throw new PlatformException("获取token失败");
            }
            return accessToken;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
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
