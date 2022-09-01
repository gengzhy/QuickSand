package xin.cosmos.basic.ocr.tencent;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.VatInvoiceOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.VatInvoiceOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.VatInvoiceVerifyRequest;
import com.tencentcloudapi.ocr.v20181119.models.VatInvoiceVerifyResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xin.cosmos.basic.ocr.baidu.util.FileUtils;
import xin.cosmos.basic.util.Base64Util;

import java.io.IOException;

@Slf4j
@Component
public class TencentOcr {

    @Autowired
    private OcrClient ocrClient;

    /**
     * 增值说发票识别
     *
     * @param imgPath
     * @return
     */
    public VatInvoiceOCRResponse vatInvoiceOcr(String imgPath) {
        try {
            // 实例化一个请求对象,每个接口都会对应一个request对象
            VatInvoiceOCRRequest req = new VatInvoiceOCRRequest();
            req.setImageBase64(getBase64Img(imgPath));
            if (isPdf(imgPath)) {
                req.setIsPdf(true);
                req.setPdfPageNumber(1L);
            }
            // 返回的resp是一个VatInvoiceOCRResponse的实例，与请求对象对应
            VatInvoiceOCRResponse resp = ocrClient.VatInvoiceOCR(req);
            // 输出json格式的字符串回包
            log.info("VAT invoice OCR response: {}", VatInvoiceOCRResponse.toJsonString(resp));
            return resp;
        } catch (TencentCloudSDKException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isPdf(String file) {
        if (StringUtils.isEmpty(file)) {
            return false;
        }
        int i = file.lastIndexOf('.');
        if (i == -1) {
            return false;
        }
        return "pdf".equalsIgnoreCase(file.substring(i + 1));
    }

    private String getBase64Img(String imgPath) throws IOException {
        return Base64Util.encode(FileUtils.readFileAsBytes(imgPath));
    }

    /**
     * 增值税发票验真
     *
     * @param req
     * @return
     */
    public VatInvoiceVerifyResponse vatInvoiceVerify(VatInvoiceVerifyRequest req) {
        try {
            VatInvoiceVerifyResponse resp = ocrClient.VatInvoiceVerify(req);
            log.info("VAT invoice OCR response: {}", VatInvoiceOCRResponse.toJsonString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }

}
