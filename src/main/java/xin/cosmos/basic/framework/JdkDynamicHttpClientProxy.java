package xin.cosmos.basic.framework;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.framework.annotation.ApiService;
import xin.cosmos.basic.framework.annotation.ApiSupport;
import xin.cosmos.basic.httpclient.HttpClient;
import xin.cosmos.basic.util.ObjectsUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * httpclient对应服务接口的动态代理，基于jdk动态代理
 *
 * @param <T>
 */
@Slf4j
public class JdkDynamicHttpClientProxy<T> implements InvocationHandler {

    private final Class<T> serviceInterfaceClass;

    public JdkDynamicHttpClientProxy(Class<T> serviceInterfaceClass) {
        this.serviceInterfaceClass = serviceInterfaceClass;
    }

    /**
     * 真正执行动态代理调用的方法执行器
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        final Class<T> serviceApiInterface = this.serviceInterfaceClass;
        ApiSupport apiSupport = serviceApiInterface.getAnnotation(ApiSupport.class);
        if (apiSupport == null) {
            throw new PlatformException("接口{%s}缺少注解{@%s}",
                    serviceApiInterface.getSimpleName(), ApiSupport.class.getSimpleName());
        }
        ApiService apiService = method.getAnnotation(ApiService.class);
        if (apiService == null) {
            throw new PlatformException("{接口方法{%s#%s}缺少注解{@%s}",
                    serviceApiInterface.getSimpleName(), method.getName(), ApiService.class.getSimpleName());
        }

        // 根节点接口与子节点接口是否对应判断
        if (!apiSupport.value().equals(apiService.value().getRootUrl())) {
            throw new PlatformException("接口{%s}注解参数{ApiRootUrl#%s}与接口方法{%s#%s}注解参数{ApiRootUrl#%s}类型不一致",
                    serviceApiInterface.getSimpleName(), apiSupport.value().name(),
                    serviceApiInterface.getSimpleName(),
                    method.getName(), apiService.value().getRootUrl().name());
        }

        // 返回类型判断
        if (Void.class.equals(method.getReturnType())) {
            throw new PlatformException("接口方法{%s#%s}必须要有返回值", serviceApiInterface.getSimpleName(), method.getName());
        }

        // 请求地址
        String fullUrl = handleUrl(apiSupport, apiService);
        log.info("请求服务接口-[{}],请求参数值-[{}] - 请求接口地址[{}]", method.getName(), Arrays.toString(args), fullUrl);

        // 格式化请求参数
        Map<String, Object> params = objectToJsonMap(args);
        String result;
        if (ApiService.RequestMethod.GET.equals(apiService.method())) {
            result = HttpClient.create().get(fullUrl, null, params);
        } else {
            result = HttpClient.create().post(fullUrl, null, params);
        }
        log.info("接口[{}]响应结果 - {}", fullUrl, result);

        // 反序列化响应结果
        if (String.class.equals(method.getReturnType())) {
            return result;
        }
        return JSON.toJavaObject(JSON.parseObject(result), method.getReturnType());
    }

    /**
     * 处理接口URL
     *
     * @param apiSupport
     * @param apiService
     * @return
     */
    private String handleUrl(ApiSupport apiSupport, ApiService apiService) {
        String rootUrl = apiSupport.value().getRootUrl();
        String api = apiService.value().getApi();

        boolean rootSlash = rootUrl.endsWith("/");
        boolean apiSlash = api.startsWith("/");
        String url;
        if (rootSlash) {
            if (apiSlash) {
                url = rootUrl + api.substring(1);
            } else {
                url = rootUrl + api;
            }
        } else {
            if (apiSlash) {
                url = rootUrl + api;
            } else {
                url = rootUrl + "/" + api;
            }
        }
        return url;
    }

    /**
     * 将JavaBean请求参数，使用fastjson转换为map
     *
     * @param params 请求参数
     * @return
     */
    private Map<String, Object> objectToJsonMap(Object[] params) {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        if (ObjectsUtil.isNull(params)) {
            return paramMap;
        }
        Object param = params[0];
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(param));
        paramMap.putAll(jsonObject);
        return paramMap;
    }
}
