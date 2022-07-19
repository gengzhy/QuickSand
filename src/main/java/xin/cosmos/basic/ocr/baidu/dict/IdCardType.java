package xin.cosmos.basic.ocr.baidu.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xin.cosmos.basic.dict.ApiDict;

/**
 * 身份证正反面标识
 */
@Getter
@AllArgsConstructor
public enum IdCardType implements ApiDict {
    FRONT("idcard_front", "身份证正面"),
    BACK("idcard_back", "身份证反面"),
    ;
    private String code;
    private String desc;
}
