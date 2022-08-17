package xin.cosmos.basic.ocr.baidu;

import com.baidu.aip.http.AipRequest;
import com.baidu.aip.ocr.AipOcr;

/**
 * Baidu OCR SDK处理公共类
 */
public final class BaiduOcr extends AipOcr {
    /**
     * 百度智能云注册的应用程序APP_ID
     */
    private static final String APP_ID = "";
    /**
     * 百度智能云注册的应用程序API_KEY
     */
    private static final String API_KEY = "";
    /**
     * 百度智能云注册的应用程序SECRET_KEY
     */
    private static final String SECRET_KEY = "";
    /**
     * 默认服务器建立连接的超时时间（单位：毫秒）
     */
    private static final int DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS = 2000;
    /**
     * 默认通过打开的连接传输数据的超时时间（单位：毫秒）
     */
    private static final int DEFAULT_SOCKET_TIMEOUT_IN_MILLIS = 60000;

    private BaiduOcr(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
        // 默认：设置网络连接参数
        super.setConnectionTimeoutInMillis(DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS);
        super.setSocketTimeoutInMillis(DEFAULT_SOCKET_TIMEOUT_IN_MILLIS);
    }

    private static class Builder {
        private final static BaiduOcr INSTANCE = new BaiduOcr(APP_ID, API_KEY, SECRET_KEY);
    }

    public static BaiduOcr create() {
        return Builder.INSTANCE;
    }

    /**
     * http代理
     * 可选：设置代理服务器地址, http和socket二选一，或者均不设置
     *
     * @param proxyHost 代理地址
     * @param proxyPort 代理端口
     * @return
     */
    public BaiduOcr httpProxy(String proxyHost, int proxyPort) {
        super.setHttpProxy(proxyHost, proxyPort);
        return this;
    }

    /**
     * http代理
     * 可选：设置代理服务器地址, http和socket二选一，或者均不设置
     *
     * @param proxyHost 代理地址
     * @param proxyPort 代理端口
     * @return
     */
    public BaiduOcr socketProxy(String proxyHost, int proxyPort) {
        super.setSocketProxy(proxyHost, proxyPort);
        return this;
    }

    /**
     * 服务器建立连接的超时时间
     *
     * @param connectionTimeoutInMillis 单位：毫秒
     * @return
     */
    public BaiduOcr connectionTimeoutInMillis(int connectionTimeoutInMillis) {
        if (connectionTimeoutInMillis <= 0) {
            return this;
        }
        super.setConnectionTimeoutInMillis(connectionTimeoutInMillis);
        return this;
    }

    /**
     * 通过打开的连接传输数据的超时时间
     *
     * @param socketTimeoutInMillis 单位：毫秒
     * @return
     */
    public BaiduOcr socketTimeoutInMillis(int socketTimeoutInMillis) {
        if (socketTimeoutInMillis <= 0) {
            return this;
        }
        super.setSocketTimeoutInMillis(socketTimeoutInMillis);
        return this;
    }

    /**
     * 身份证正面识别接口
     *
     * @param image 身份证正面图像
     * @return
     */
    public String idCardFront(byte[] image) {
        return super.idcard(image, "front", null).toString();
    }

    /**
     * 身份证正面识别接口
     *
     * @param image 身份证正面图像
     * @return
     */
    public String idCardBack(byte[] image) {
        return super.idcard(image, "back", null).toString();
    }

    /**
     * 身份证正面识别接口
     *
     * @param imageUrl 身份证正面图像url地址
     * @return
     */
    public String idCardFront(String imageUrl) {
        return doIdCardByUrl(BaiduOcrUriDict.IDCARD.uri(), imageUrl, "front");
    }

    /**
     * 身份证反面识别接口
     *
     * @param imageUrl 身份证反面图像url地址
     * @return
     */
    public String idCardBack(String imageUrl) {
        return doIdCardByUrl(BaiduOcrUriDict.IDCARD.uri(), imageUrl, "back");
    }

    /**
     * 身份证正反面识别接口
     *
     * @param image 身份证正反面图像url地址
     * @return
     */
    public String idCardMulti(byte[] image) {
        return super.multiIdcard(image, null).toString();
    }

    /**
     * 身份证正反面识别接口
     *
     * @param imageUrl 身份证正反面图像url地址
     * @return
     */
    public String idCardMulti(String imageUrl) {
        return doIdCardByUrl(BaiduOcrUriDict.MULTI_IDCARD.uri(), imageUrl, null);
    }

    /**
     * 营业执照识别接口
     * @param businessLicense 营业执照
     * @return
     */
    public String businessLicense(byte[] businessLicense) {
        return super.businessLicense(businessLicense, null).toString();
    }

    /**
     * 营业执照识别接口
     * @param businessLicenseUrl 营业执照url地址
     * @return
     */
    public String businessLicense(String businessLicenseUrl) {
        AipRequest request = new AipRequest();
        request.setUri(BaiduOcrUriDict.BUSINESS_LICENSE.uri());
        preOperation(request);
        request.addBody("detect_direction", true);
        request.addBody("url", businessLicenseUrl);
        postOperation(request);
        return super.requestServer(request).toString();
    }

    /**
     * 身份证识别-传递身份证图片的url地址
     * @param ocrUri OCR识别地址
     * @param imageUrl 身份证图片地址
     * @param idCardSide 身份证正反面
     * @return
     */
    private String doIdCardByUrl(String ocrUri, String imageUrl, String idCardSide) {
        AipRequest request = new AipRequest();
        super.preOperation(request);
        request.addBody("url", imageUrl);
        if (idCardSide != null) {
            request.addBody("id_card_side", idCardSide);
        }
        request.setUri(ocrUri);
        super.postOperation(request);
        return super.requestServer(request).toString();
    }
}
