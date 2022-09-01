package xin.cosmos;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.models.VatInvoiceVerifyRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xin.cosmos.basic.ocr.tencent.TencentOcr;

@SpringBootTest
public class TencentOcrTest {
    @Autowired
    private TencentOcr tencentOcr;

    @Test
    public void vatInvoice() {
        String imageUrl = "e:/my/images/vat_invoice.pdf";
        tencentOcr.vatInvoiceOcr(imageUrl);
    }

    @Test
    public void vatInvoiceVerify() throws TencentCloudSDKException {
        VatInvoiceVerifyRequest req = new VatInvoiceVerifyRequest();
        req.setInvoiceNo("36731183");
        req.setInvoiceCode("0110021005110");
        req.setInvoiceDate("2022-08-15");
        req.setAdditional("610240");
        tencentOcr.vatInvoiceVerify(req);
    }
}


