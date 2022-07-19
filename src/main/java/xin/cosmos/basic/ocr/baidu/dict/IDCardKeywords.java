package xin.cosmos.basic.ocr.baidu.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 身份证关键字枚举字典
 */
public interface IDCardKeywords {
    String FRONT_NAME = "姓名";
    String FRONT_SEX = "性别";
    String FRONT_NATION = "民族";
    String FRONT_BIRTH = "出生";
    String FRONT_ADDRESS = "住址";
    String FRONT_ID = "公民身份号码";
    String BACK_SIGN_ORG = "签发机关";
    String BACK_SIGN_DATE = "签发日期";
    String BACK_EXPIRE = "失效日期";
}
