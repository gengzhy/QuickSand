package xin.cosmos.basic.ocr.baidu;

import com.baidu.aip.contentcensor.EImgType;
import com.baidu.aip.http.AipRequest;
import com.baidu.aip.ocr.AipOcr;
import com.baidu.aip.util.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.ocr.baidu.model.BusinessLicense;
import xin.cosmos.basic.ocr.baidu.model.IdCard;
import xin.cosmos.basic.ocr.baidu.parser.BusinessLicenseParser;
import xin.cosmos.basic.ocr.baidu.parser.IDCardParser;
import xin.cosmos.basic.ocr.baidu.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Baidu OCR SDK处理公共类
 */
@Slf4j
@Component
public final class BaiduOcr implements InitializingBean {
    /**
     * 要添加到每个缩进级别的空格数
     */
    private final static int INDENT_FACTOR = 0;
    /**
     * 百度智能云注册的应用程序APP_ID
     */
    @Value("${ocr.baidu.appId}")
    private String appId;
    /**
     * 百度智能云注册的应用程序API_KEY
     */
    @Value("${ocr.baidu.appKey}")
    private String appKey;
    /**
     * 百度智能云注册的应用程序SECRET_KEY
     */
    @Value("${ocr.baidu.secretKey}")
    private String secretKey;

    /**
     * OCR识别处理的核心
     */
    private Core core;


    /**
     * 身份证正面识别接口
     *
     * @param image 身份证正面图像
     * @return
     */
    public IdCard idCardFront(byte[] image) {
        return new IDCardParser().parse(core.idcard(image, "front", null).toString(INDENT_FACTOR));
    }

    /**
     * 身份证正面识别接口
     *
     * @param image 身份证正面图像
     * @return
     */
    public IdCard idCardFront(File image) throws IOException {
        return idCardFront(FileUtils.readFileAsBytes(image));
    }

    /**
     * 身份证正面识别接口
     *
     * @param image 身份证正面图像
     * @return
     */
    public IdCard idCardFront(MultipartFile image) throws IOException {
        return idCardFront(FileUtils.readFileAsBytes(image));
    }

    /**
     * 身份证正面识别接口
     *
     * @param imageUrl 身份证正面图像url地址
     * @return
     */
    public IdCard idCardFront(String imageUrl) {
        return core.doIdCardByUrl(BaiduOcrUriDict.IDCARD.uri(), imageUrl, "front");
    }

    /**
     * 身份证反面识别接口
     *
     * @param image 身份证正面图像
     * @return
     */
    public IdCard idCardBack(byte[] image) {
        return new IDCardParser().parse(core.idcard(image, "back", null).toString(INDENT_FACTOR));
    }

    /**
     * 身份证反面识别接口
     *
     * @param image 身份证正面图像
     * @return
     */
    public IdCard idCardBack(File image) throws IOException {
        return idCardBack(FileUtils.readFileAsBytes(image));
    }

    /**
     * 身份证反面识别接口
     *
     * @param image 身份证正面图像
     * @return
     */
    public IdCard idCardBack(MultipartFile image) throws IOException {
        return idCardBack(FileUtils.readFileAsBytes(image));
    }

    /**
     * 身份证反面识别接口
     *
     * @param imageUrl 身份证反面图像url地址
     * @return
     */
    public IdCard idCardBack(String imageUrl) {
        return core.doIdCardByUrl(BaiduOcrUriDict.IDCARD.uri(), imageUrl, "back");
    }

    /**
     * 身份证正反面识别接口
     *
     * @param image 身份证正反面图像
     * @return
     */
    public IdCard idCardMulti(byte[] image) {
        return new IDCardParser().parse(core.multiIdcard(image, null).toString(INDENT_FACTOR));
    }

    /**
     * 身份证正反面识别接口
     *
     * @param image 身份证正反面图像
     * @return
     */
    public IdCard idCardMulti(File image) throws IOException {
        return idCardMulti(FileUtils.readFileAsBytes(image));
    }

    /**
     * 身份证正反面识别接口
     *
     * @param image 身份证正反面图像
     * @return
     */
    public IdCard idCardMulti(MultipartFile image) throws IOException {
        return idCardMulti(FileUtils.readFileAsBytes(image));
    }

    /**
     * 身份证正反面识别接口
     *
     * @param imageUrl 身份证正反面图像url地址
     * @return
     */
    public IdCard idCardMulti(String imageUrl) {
        return core.doIdCardByUrl(BaiduOcrUriDict.MULTI_IDCARD.uri(), imageUrl, null);
    }

    /**
     * 营业执照识别接口
     *
     * @param businessLicense 营业执照
     * @return
     */
    public BusinessLicense businessLicense(byte[] businessLicense) {
        return new BusinessLicenseParser().parse(core.businessLicense(businessLicense, null).toString(INDENT_FACTOR));
    }

    /**
     * 营业执照识别接口
     *
     * @param businessLicense 营业执照
     * @return
     */
    public BusinessLicense businessLicense(File businessLicense) throws IOException {
        return businessLicense(FileUtils.readFileAsBytes(businessLicense));
    }

    /**
     * 营业执照识别接口
     *
     * @param businessLicense 营业执照
     * @return
     */
    public BusinessLicense businessLicense(MultipartFile businessLicense) throws IOException {
        return businessLicense(FileUtils.readFileAsBytes(businessLicense));
    }

    /**
     * 营业执照识别接口
     *
     * @param businessLicenseUrl 营业执照url地址
     * @return
     */
    public BusinessLicense businessLicense(String businessLicenseUrl) {
        return core.doBusinessLicense(businessLicenseUrl);
    }

    /**
     * 增值税发票识别接口 - 图片格式
     *
     * @param image 增值税发票
     * @return
     */
    public String vatInvoiceImage(byte[] image) {
        return core.doVatInvoice(image, EImgType.FILE, null);
    }

    /**
     * 增值税发票识别接口 - 图片格式
     *
     * @param image 增值税发票
     * @return
     */
    public String vatInvoiceImage(File image) throws IOException {
        return vatInvoiceImage(FileUtils.readFileAsBytes(image));
    }

    /**
     * 增值税发票识别接口 - 图片格式
     *
     * @param image 增值税发票
     * @return
     */
    public String vatInvoiceImage(MultipartFile image) throws IOException {
        return vatInvoiceImage(FileUtils.readFileAsBytes(image));
    }

    /**
     * 增值税发票识别接口 - PDF格式
     *
     * @param image 增值税发票
     * @return
     */
    public String vatInvoiceImagePdf(byte[] image) {
        return core.doVatInvoice(image, EImgType.PDF, null);
    }

    /**
     * 增值税发票识别接口 - PDF格式
     *
     * @param image 增值税发票
     * @return
     */
    public String vatInvoiceImagePdf(File image) throws IOException {
        return vatInvoiceImagePdf(FileUtils.readFileAsBytes(image));
    }

    /**
     * 增值税发票识别接口 - PDF格式
     *
     * @param image 增值税发票
     * @return
     */
    public String vatInvoiceImagePdf(MultipartFile image) throws IOException {
        return vatInvoiceImagePdf(FileUtils.readFileAsBytes(image));
    }

    /**
     * OCR识别处理的核心类
     */
    static final class Core extends AipOcr {
        /**
         * 默认服务器建立连接的超时时间（单位：毫秒）
         */
        static final int DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS = 2000;
        /**
         * 默认通过打开的连接传输数据的超时时间（单位：毫秒）
         */
        static final int DEFAULT_SOCKET_TIMEOUT_IN_MILLIS = 60000;

        Core(String appId, String apiKey, String secretKey) {
            super(appId, apiKey, secretKey);
            setConnectionTimeoutInMillis(DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS);
            setSocketTimeoutInMillis(DEFAULT_SOCKET_TIMEOUT_IN_MILLIS);
        }

        /**
         * 图像url方式识别公共处理
         *
         * @param ocrUri OCR地址
         * @param imageUrl 图像的url
         * @param options 其他可选参数
         * @return
         */
        String doCommonImageUrl(String ocrUri, String imageUrl, HashMap<String, Object> options) {
            AipRequest request = new AipRequest();
            preOperation(request);
            request.setUri(ocrUri);
            request.addBody("url", imageUrl);
            if (options != null) {
                request.addBody(options);
            }
            postOperation(request);
            return requestServer(request).toString(INDENT_FACTOR);
        }

        /**
         * 身份证识别-传递身份证图片的url地址
         *
         * @param ocrUri     OCR识别地址
         * @param imageUrl   身份证图片地址
         * @param idCardSide 身份证正反面：front-正面；back-反面
         * @return
         */
        IdCard doIdCardByUrl(String ocrUri, String imageUrl, String idCardSide) {
            HashMap<String, Object> params = null;
            if (idCardSide != null) {
                params = new HashMap<>();
                params.put("id_card_side", idCardSide);
            }
            return new IDCardParser().parse(doCommonImageUrl(ocrUri, imageUrl, params));
        }

        /**
         * 营业执照识别接口
         *
         * @param businessLicenseUrl 营业执照url地址
         * @return
         */
        BusinessLicense doBusinessLicense(String businessLicenseUrl) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("detect_direction", true);
            return new BusinessLicenseParser().parse(doCommonImageUrl(BaiduOcrUriDict.BUSINESS_LICENSE.uri(), businessLicenseUrl, params));
        }

        /**
         * 增值税发票识别接口
         * 注：当指定的文件是PDF时，需要在options中添加key为"pdf_file_num"并指定页码，默认为第一页
         *
         * @param image   增值税发票
         * @param type    文件类型
         * @param options 可选参数对象，key: value都为string类型
         * @return
         */
        String doVatInvoice(byte[] image, EImgType type, HashMap<String, Object> options) {
            AipRequest request = new AipRequest();
            preOperation(request);
            String base64Content = Base64Util.encode(image);
            if (EImgType.FILE.equals(type)) {
                request.addBody("image", base64Content);
            } else if (EImgType.PDF.equals(type)) {
                request.addBody("pdf_file", base64Content);
                // 默认第一页
                request.addBody("pdf_file_num", "1");
            }
            // 是否开启印章判断功能，并返回印章内容的识别结果：true/false
            request.addBody("seal_tag", true);
            if (options != null) {
                request.addBody(options);
            }
            request.setUri(BaiduOcrUriDict.VAT_INVOICE.uri());
            postOperation(request);
            return requestServer(request).toString(INDENT_FACTOR);
        }
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
        core.setHttpProxy(proxyHost, proxyPort);
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
        core.setSocketProxy(proxyHost, proxyPort);
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
        core.setConnectionTimeoutInMillis(connectionTimeoutInMillis);
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
        core.setSocketTimeoutInMillis(socketTimeoutInMillis);
        return this;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.core = new Core(appId, appKey, secretKey);
    }
}
