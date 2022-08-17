package xin.cosmos.basic.ocr.baidu.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.ocr.baidu.dict.LicenseKeywords;
import xin.cosmos.basic.ocr.model.BusinessLicense;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * OCR营业执照识别结果解析
 */
@Slf4j
public class BusinessLicenseParser implements ResponseParser<BusinessLicense> {
    /**
     * 营业执照日期格式化
     */
    final static ThreadLocal<DateFormat> licenseDateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy年MM月dd日"));

    @Override
    public BusinessLicense parse(String json) {
        log.info("营业执照OCR识别结果=>{}", json);
        JSONObject result = JSON.parseObject(json).getJSONObject("words_result");
        if (result == null) {
            throw new PlatformException("识别营业执照接口返回信息状态失败");
        }
        BusinessLicense license = new BusinessLicense();
        buildBusinessLicense(license, result);
        return license;
    }
    /**
     * 构建营业执照信息实体对象
     *
     * @param license 营业执照对象
     * @param meta    接口返回营业执照元数据
     */
    private void buildBusinessLicense(BusinessLicense license, JSONObject meta) {
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

}
