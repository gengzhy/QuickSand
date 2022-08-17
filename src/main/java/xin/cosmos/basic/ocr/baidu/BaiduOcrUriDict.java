package xin.cosmos.basic.ocr.baidu;

import com.baidu.aip.ocr.OcrConsts;
import xin.cosmos.basic.exception.PlatformException;

import java.lang.reflect.Field;

/**
 * 百度OCR接口地址字典
 * 映射到{@code com.baidu.aip.ocr.OcrConsts}的地址
 */
public enum BaiduOcrUriDict {
    GENERAL_BASIC("GENERAL_BASIC"),
    ACCURATE_BASIC("ACCURATE_BASIC"),
    GENERAL("GENERAL"),
    ACCURATE("ACCURATE"),
    GENERAL_ENHANCED("GENERAL_ENHANCED"),
    WEB_IMAGE("WEB_IMAGE"),
    IDCARD("IDCARD"),
    BANKCARD("BANKCARD"),
    DRIVING_LICENSE("DRIVING_LICENSE"),
    VEHICLE_LICENSE("VEHICLE_LICENSE"),
    LICENSE_PLATE("LICENSE_PLATE"),
    BUSINESS_LICENSE("BUSINESS_LICENSE"),
    RECEIPT("RECEIPT"),
    TRAIN_TICKET("TRAIN_TICKET"),
    TAXI_RECEIPT("TAXI_RECEIPT"),
    FORM("FORM"),
    TABLE_RECOGNIZE("TABLE_RECOGNIZE"),
    TABLE_RESULT_GET("TABLE_RESULT_GET"),
    VIN_CODE("VIN_CODE"),
    QUOTA_INVOICE("QUOTA_INVOICE"),
    HOUSEHOLD_REGISTER("HOUSEHOLD_REGISTER"),
    HK_MACAU_EXITENTRYPERMIT("HK_MACAU_EXITENTRYPERMIT"),
    TAIWAN_EXITENTRYPERMIT("TAIWAN_EXITENTRYPERMIT"),
    BIRTH_CERTIFICATE("BIRTH_CERTIFICATE"),
    VEHICLE_INVOICE("VEHICLE_INVOICE"),
    VEHICLE_CERTIFICATE("VEHICLE_CERTIFICATE"),
    INVOICE("INVOICE"),
    AIR_TICKET("AIR_TICKET"),
    INSURANCE_DOCUMENTS("INSURANCE_DOCUMENTS"),
    VAT_INVOICE("VAT_INVOICE"),
    QRCODE("QRCODE"),
    NUMBERS("NUMBERS"),
    LOTTERY("LOTTERY"),
    PASSPORT("PASSPORT"),
    BUSINESS_CARD("BUSINESS_CARD"),
    HANDWRITING("HANDWRITING"),
    CUSTOM("CUSTOM"),
    DOC_ANALYSIS("DOC_ANALYSIS"),
    METER("METER"),
    WEBIMAGE_LOC("WEBIMAGE_LOC"),
    ASYNC_TASK_STATUS_FINISHED("ASYNC_TASK_STATUS_FINISHED"),
    WEIGHT_NOTE("WEIGHT_NOTE"),
    ONLINE_TAXI_ITINERARY("ONLINE_TAXI_ITINERARY"),
    MEDICAL_DETAIL("MEDICAL_DETAIL"),
    DOC_ANALYSIS_OFFICE("DOC_ANALYSIS_OFFICE"),
    SEAL("SEAL"),
    MULTI_IDCARD("MULTI_IDCARD"),
    SOCIAL_SECURITY_CARD("SOCIAL_SECURITY_CARD"),
    MULTI_CARD_CLASSIFY("MULTI_CARD_CLASSIFY"),
    MIXED_MULTI_VEHICLE("MIXED_MULTI_VEHICLE"),
    USED_VEHICLE_INVOICE("USED_VEHICLE_INVOICE"),
    VEHICLE_REGISTRATION_CERTIFICATE("VEHICLE_REGISTRATION_CERTIFICATE"),
    WAYBILL("WAYBILL"),
    ROAD_TRANSPORT_CERTIFICATE("ROAD_TRANSPORT_CERTIFICATE"),
    MULTIPLE_INVOICE("MULTIPLE_INVOICE"),
    VAT_INVOICE_VERIFICATION("VAT_INVOICE_VERIFICATION"),
    BUS_TICKET("BUS_TICKET"),
    TOLL_INVOICE("TOLL_INVOICE"),
    FERRY_TICKET("FERRY_TICKET"),
    SHOPPING_RECEIPT("SHOPPING_RECEIPT"),
    MEDICAL_INVOICE("MEDICAL_INVOICE"),
    MEDICAL_STATEMENT("MEDICAL_STATEMENT"),
    MEDICAL_REPORT_DETECTION("MEDICAL_REPORT_DETECTION"),
    MEDICAL_RECORD("MEDICAL_RECORD"),
    MEDICAL_SUMMARY("MEDICAL_SUMMARY"),
    MEDICAL_RECIPTS_CLASSIFY("MEDICAL_RECIPTS_CLASSIFY"),
    FORMULA("FORMULA"),
    TRAVEL_CARD("TRAVEL_CARD"),
    FACADE("FACADE"),
    INTELLIGENT_OCR("INTELLIGENT_OCR"),
    ;
    private final String ocrUriCode;
    BaiduOcrUriDict(String ocrUriCode) {
        this.ocrUriCode = ocrUriCode;
    }

    /**
     * 拿到OCRurl地址
     * @return String
     */
    public String uri() {
        try {
            OcrConsts instance = OcrConsts.class.newInstance();
            for (Field field : OcrConsts.class.getDeclaredFields()) {
                if (this.ocrUriCode.equals(field.getName())) {
                    field.setAccessible(true);
                    return (String) field.get(instance);
                }
            }
            throw new PlatformException("未获取到字典【" + this.name() + "】对应的OCR地址，请更新维护！");
        } catch (Exception e) {
            throw new PlatformException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("INTELLIGENT_OCR.getBaiduOcrUri() = " + MULTI_IDCARD.uri());
    }

}
