package xin.cosmos.basic.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
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

    /**
     * 代理IP或地址{@code enableProxy启用时生效}
     */
    private String proxyIpOrHost;

    /**
     * 代理端口{@code enableProxy启用时生效}
     */
    private int proxyPort;

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
     * 设置代理主机IP或地址
     *
     * @param proxyIpOrHost 代理IP或地址
     * @return
     */
    public HttpClient proxyHost(String proxyIpOrHost) {
        this.proxyIpOrHost = proxyIpOrHost;
        return this;
    }

    /**
     * 设置代理端口
     *
     * @param proxyPort 代理端口
     * @return
     */
    public HttpClient proxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
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
        HttpClientTool httpClientTool = HttpClientTool.create(chc, charset);
        // 设置代理
        httpClientTool.proxyHost(new HttpHost(this.proxyIpOrHost, this.proxyPort));
        return httpClientTool.doPost(url, headers, params);
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
        Map<String, Object> map = objectToMap(params);
        HttpClientTool httpClientTool = HttpClientTool.create(chc, charset);
        // 设置代理
        httpClientTool.proxyHost(new HttpHost(this.proxyIpOrHost, this.proxyPort));
        return httpClientTool.doGet(url, headers, map);
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
        // 如果是数组，取第0个元素
        if (obj.getClass().isArray()) {
            obj = ((Object[]) obj)[0];
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
