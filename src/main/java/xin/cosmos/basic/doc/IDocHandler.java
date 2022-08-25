package xin.cosmos.basic.doc;

import java.io.IOException;
import java.util.Map;

public interface IDocHandler {
    // 特殊字符
    char SPECIAL_CHAR = '\u0007';
    char SPECIAL_CHAR_R = '\r';
    char SPECIAL_CHAR_N = '\n';

    Map<String, Object> read();

    IDocHandler fill(Map<String, Object> params);

    void writeAutoClose(String toPath) throws IOException;


    void close() throws IOException;

    /**
     * 是否是特殊字符
     * @param text
     * @return
     */
    default  boolean isOnlySpecialChar(String text) {
        int len = text.length();
        char c = text.charAt(0);
        return len == 1 && (c == SPECIAL_CHAR || c == SPECIAL_CHAR_R || c == SPECIAL_CHAR_N);
    }
}
