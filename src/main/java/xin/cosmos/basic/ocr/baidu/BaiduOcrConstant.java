package xin.cosmos.basic.ocr.baidu;

/**
 * 百度OCR识别配置信息
 */
public interface BaiduOcrConstant {
    /**
     * 编码
     */
    String CHARSET = "UTF-8";

    /**
     * 百度智能云注册的应用程序API Key
     */
    String CLIENT_ID = "百度智能云注册的应用程序API Key";

    /**
     * 百度智能云注册的应用程序Secret Key
     */
    String CLIENT_SECRET = "百度智能云注册的应用程序Secret Key";

    /**
     * 身份证正面识别OCR接口地址
     */
    String ID_CARD_FRONT_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";

    /**
     * 身份证混贴识别OCR接口地址
     */
    String ID_CARD_MULTI_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/multi_idcard";

    /**
     * 营业执照识别OCR接口地址
     */
    String BUSINESS_LICENSE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license";

    /**
     * OCR accessToken获取接口地址
     */
    String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
}
