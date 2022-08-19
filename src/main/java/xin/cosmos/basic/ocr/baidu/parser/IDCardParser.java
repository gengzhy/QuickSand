package xin.cosmos.basic.ocr.baidu.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.ocr.baidu.field.IDCardKeywords;
import xin.cosmos.basic.ocr.baidu.model.IdCard;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * OCR身份证识别结果解析
 */
@Slf4j
public class IDCardParser implements ResponseParser<IdCard> {
    /**
     * 身份证日期格式化
     */
    final static ThreadLocal<DateFormat> idCardDateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    @Override
    public IdCard parse(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        log.info("身份证OCR识别结果=>{}", json);
        Object result = jsonObject.get("words_result");
        if (result == null) {
            throw new PlatformException("没有解析到任何数据");
        }
        IdCard idCard = new IdCard();
        if (result instanceof JSONObject) {
            buildIdCard(idCard, jsonObject.getJSONObject("words_result"));
        } else if (result instanceof JSONArray) {
            JSONArray wordsResults = jsonObject.getJSONArray("words_result");
            for (int i = 0; i < wordsResults.size(); i++) {
                JSONObject cardResult = wordsResults.getJSONObject(i).getJSONObject("card_result");
                if (cardResult == null) {
                    continue;
                }
                buildIdCard(idCard, cardResult);
            }
        }
        return idCard;
    }

    /**
     * 构建身份证信息实体对象
     *
     * @param idCard 身份证对象
     * @param meta   接口返回身份证元数据
     */
    private void buildIdCard(IdCard idCard, JSONObject meta) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Map.Entry<String, Object> entry : meta.entrySet()) {
            String key = entry.getKey();
            String resultStr = entry.getValue().toString();
            JSONObject result = JSON.parseObject(resultStr);
            String value = result.getString("words");
            switch (key) {
                case IDCardKeywords.FRONT_NAME:
                    idCard.setName(value);
                    break;
                case IDCardKeywords.FRONT_SEX:
                    idCard.setSex(value);
                    break;
                case IDCardKeywords.FRONT_NATION:
                    idCard.setNation(value);
                    break;
                case IDCardKeywords.FRONT_BIRTH:
                    if (StringUtils.isNotBlank(value) && !"无".equals(value)) {
                        try {
                            idCard.setBirthDate(dateFormat.format(idCardDateFormat.get().parse(value)));
                        } catch (ParseException e) {
                           log.error("出生日期解析错误", e);
                        }
                    }
                    break;
                case IDCardKeywords.FRONT_ADDRESS:
                    idCard.setAddress(value);
                    break;
                case IDCardKeywords.FRONT_ID:
                    idCard.setCardNumber(value);
                    break;
                case IDCardKeywords.BACK_SIGN_ORG:
                    idCard.setSignOrg(value);
                    break;
                case IDCardKeywords.BACK_SIGN_DATE:
                    if (StringUtils.isNotBlank(value) && !"无".equals(value)) {
                        try {
                            idCard.setSignDate(dateFormat.format(idCardDateFormat.get().parse(value)));
                        } catch (ParseException e) {
                            log.error("签发日期解析错误", e);
                        }
                    }
                    break;
                case IDCardKeywords.BACK_EXPIRE:
                    if (StringUtils.isNotBlank(value) && !"无".equals(value)) {
                        if ("长期".equals(value)) {
                            idCard.setExpiredDate(value);
                        } else {
                            try {
                                idCard.setExpiredDate(dateFormat.format(idCardDateFormat.get().parse(value)));
                            } catch (ParseException e) {
                                log.error("失效日期解析错误", e);
                            }
                        }
                    }
                    break;
                default:
            }
        }
    }
}
