package xin.cosmos.basic.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import xin.cosmos.basic.util.ObjectsUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * httpClient 工具类
 */
@Slf4j
class HttpClientTool {

    private CloseableHttpClient httpClient;
    public String charset = "UTF-8";

    private HttpClientTool() {
    }

    protected static HttpClientTool create(CloseableHttpClient httpClient, String charset) {
        HttpClientTool clientTool = new HttpClientTool();
        clientTool.httpClient = httpClient;
        if (StringUtils.isNotBlank(charset)) {
            clientTool.charset = charset;
        }
        return clientTool;
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url    请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @return 响应数据
     */
    protected String doGet(String url, Map<String, Object> params) {
        return doGet(url, null, params);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param headers 请求头参数
     * @param params  请求的参数
     * @return 响应数据
     */
    protected String doGet(String url, Map<String, String> headers, Map<String, Object> params) {
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = paramsConvert(params);
                // 将请求参数和url进行拼接
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头参数
            ObjectsUtil.nonNullTodo(headers, () -> headers.forEach(httpGet::setHeader));
            logInfo(httpGet, url, params);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            if (statusCode != HttpStatus.SC_OK) {
                log.error("HttpClient Get handle failed: {}-{}", statusCode, reasonPhrase);
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);
            response.close();
            logResult(result);
            return result;
        } catch (Exception e) {
            log.error("Httpclient 与第三方通信异常", e);
            throw new RuntimeException("与第三方通信异常:" + e.getMessage());
        }
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url    请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @return 页面内容
     * @throws IOException
     */
    protected String doPost(String url, Object params) {
        return this.doPost(url, null, params);
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param headers 请求头参数
     * @param params  请求的参数
     * @return 页面内容
     * @throws IOException
     */
    protected String doPost(String url, Map<String, String> headers, Object params) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头参数
        ObjectsUtil.nonNullTodo(headers, () -> headers.forEach(httpPost::setHeader));

        httpPost.setHeader("Content-Type", ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8).getMimeType());
        httpPost.setEntity(new StringEntity(params.toString(), StandardCharsets.UTF_8));
        logInfo(httpPost, url, params);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                log.error("HttpClient Post handle failed: {}-{}", statusCode, reasonPhrase);
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, this.charset);
            }
            EntityUtils.consume(entity);
            logResult(result);
            return result;
        } catch (Exception e) {
            log.error("Httpclient 与第三方通信异常", e);
            throw new RuntimeException("与第三方通信异常");
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("response 关闭异常", e);
                }
            }
        }
    }

    /**
     * map to NameValuePair
     *
     * @param params
     * @return
     */
    private List<NameValuePair> paramsConvert(Map<String, Object> params) {
        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value.toString()));
                }
            }
        }
        return pairs;
    }

    /**
     * 打印接口请求日志信息
     *
     * @param httpMessage
     * @param url
     * @param params
     */
    private void logInfo(AbstractHttpMessage httpMessage, String url, Object params) {
        if (!log.isInfoEnabled()) {
            return;
        }
        log.info("╔═════════════════════════════════════════════════════════");
        log.info("║—————请求URL:");
        log.info("║{}", url);
        log.info("║—————请求头:");
        Header[] headers = httpMessage.getAllHeaders();
        Arrays.stream(headers).forEach(header -> log.info("║{} => {}", header.getName(), header.getValue()));
        log.info("║—————请求参数:");
        if (params instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) params;
            map.forEach((k, v) -> log.info("║{} => {}", k, v));
        } else {
            log.info("║{}", params.toString());
        }
    }

    /**
     * 打印响应结果
     *
     * @param result
     */
    private void logResult(String result) {
        if (!log.isInfoEnabled()) {
            return;
        }
        log.info("║—————响应结果:");
        log.info("║{}", result);
        log.info("╚═════════════════════════════════════════════════════════");
    }
}