package xin.cosmos.basic.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * httpclient 工具
 */
@Slf4j
public class HttpClient {

    /**
     * 默认编码
     */
    private final String charset = "UTF-8";
    /**
     * 默认超时时间 单位：毫秒
     */
    private final int timeout = 60000;

    private final static String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.7 Safari/537.36 Edg/101.0.1210.39";

    /**
     * 请求头-仿照浏览器发送请求
     */
    private String userAgent;

    public HttpClient userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * 默认请求头参数
     * 仅包含User-Agent.如果没有初始化，则采用默认的userAgent
     *
     * @return
     */
    private Map<String, String> userAgent() {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("User-Agent", Optional.ofNullable(this.userAgent).orElse(DEFAULT_USER_AGENT));
        return headers;
    }

    private HttpClient() {
    }

    /**
     * @return
     * @例子：HttpClient.cateate().charset("utf-8").post(url, param);
     */
    public static HttpClient create() {
        return new HttpClient();
    }

    /**
     * post请求
     *
     * @param url    请求的url地址 ?之前的地址
     * @param params 请求参数
     * @return
     */
    public String post(String url, Object params) {
        return this.post(url, null, params);
    }

    /**
     * post请求
     *
     * @param url     请求的url地址 ?之前的地址
     * @param headers 请求头参数
     * @param params  请求参数
     * @return
     */
    public String post(String url, Map<String, String> headers, Object params) {
        CloseableHttpClient chc = this.initHttpClientCustBuild(url).timeout(timeout).build();
        headers = Optional.ofNullable(headers).orElse(this.userAgent());
        return HttpClientTool.create(chc, charset).doPost(url, headers, params);
    }

    /**
     * get请求
     *
     * @param url    请求的url地址 ?之前的地址
     * @param params 请求参数
     * @return
     */
    public String get(String url, Object params) {
        return this.get(url, null, params);
    }

    /**
     * get请求
     *
     * @param url     请求的url地址 ?之前的地址
     * @param headers 请求头参数
     * @param params  请求参数
     * @return
     */
    public String get(String url, Map<String, String> headers, Object params) {
        HttpClientCustBuild httpClientCustBuild = this.initHttpClientCustBuild(url);
        CloseableHttpClient chc = httpClientCustBuild.timeout(timeout).build();
        headers = Optional.ofNullable(headers).orElse(this.userAgent());
        Map<String, Object> map = objectToMap(params);
        return HttpClientTool.create(chc, charset).doGet(url, headers, map);
    }

    private HttpClientCustBuild initHttpClientCustBuild(String url) {
        if (url.toLowerCase().startsWith("https://")) {
            return HttpClientCustBuild.getInstance().ssl();
        } else {
            return HttpClientCustBuild.getInstance();
        }
    }

    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return new HashMap<>(0);
        }
        if (obj instanceof Map<?, ?>) {
            return (Map<String, Object>) obj;
        }
        Map<String, Object> map = new LinkedHashMap<>(obj.getClass().getDeclaredFields().length);
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
            map.put(fieldName, value);
        }
        return map;
    }

}
