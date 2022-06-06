package xin.cosmos.basic.framework.header;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xin.cosmos.basic.util.ObjectsUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 动态请求头
 */
@AllArgsConstructor
@Getter
public enum DynamicHeaders {
    /**
     * 无请求头
     */
    NONE(null) {
        @Override
        public DynamicHeaders appendHeaders(Map<String, String> headers) {
            return null;
        }
    },

    /**
     * 上海票交所披露信息查询
     */
    SHCPE_DISCLOSURE(new LinkedHashMap<String, String>() {{
        put("Accept", "application/json, text/plain, */*");
        put("Accept-Encoding", "gzip, deflate, br");
        put("Accept-Language", "zh-CN,zh;q=0.9");
        put("Authorization", "");
        put("Connection", "keep-alive");
        put("DNT", "1");
        put("Host", "disclosure.shcpe.com.cn");
        put("Referer", "https://disclosure.shcpe.com.cn/");
        put("sec-ch-ua", "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
        put("sec-ch-ua-mobile", "?0");
        put("sec-ch-ua-platform", "\"Windows\"");
        put("Sec-Fetch-Dest", "empty");
        put("Sec-Fetch-Mode", "cors");
        put("Sec-Fetch-Site", "same-origin");
        put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
    }}) {
        @Override
        public DynamicHeaders appendHeaders(Map<String, String> headers) {
            if (!ObjectsUtil.isNull(headers)) {
                this.getHeaders().putAll(headers);
            }
            return this;
        }
    },

    
    ;

    /**
     * 请求头
     */
    private final Map<String, String> headers;

    /**
     * 添加请求头参数
     *
     * @param headers 请求头
     * @return
     */
    public abstract DynamicHeaders appendHeaders(Map<String, String> headers);

}
