package xin.cosmos.basic.ocr.baidu.parser;

import java.text.ParseException;

/**
 * OCR响应结果解析器
 */
public interface ResponseParser<T> {

    /**
     * 解析百度OCR识别结果JSON字符串为目标实体结果
     *
     * @param json
     * @return
     */
    T parse(String json) throws ParseException;
}
