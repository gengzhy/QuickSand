package xin.cosmos.test.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 商户信息元数据
 */
@Data
public class MerchantMetaData {

    @ExcelProperty(value = "商户编号")
    private String code;

    /**
     * 个体工商户，企业法人，小微商户
     */
    @ExcelProperty(value = "商户类型")
    private String corpName;

    @ExcelProperty(value = "营业执照号")
    private String licence;

    @ExcelProperty(value = "身份证正面")
    private String idCardFrontImg;

    @ExcelProperty(value = "身份证反面")
    private String idCardBackImg;

    @ExcelProperty(value = "营业执照图片")
    private String licenceImg;

    @ExcelProperty(value = "身份证签发日期")
    private String signDate;

    @ExcelProperty(value = "身份证失效日期")
    private String expiredDate;

    @ExcelProperty(value = "营业执照成立日期")
    private String companyCreateDate;

    @ExcelProperty(value = "营业执照有效期")
    private String validity;
}
