package xin.cosmos.basic.framework;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.framework.annotation.ApiService;
import xin.cosmos.basic.framework.annotation.ApiServiceOperation;
import xin.cosmos.basic.framework.enums.RequestMethod;
import xin.cosmos.basic.httpclient.HttpClient;

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
    private Class<T> serviceInterfaceClass;

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
        ApiService apiService = serviceApiInterface.getAnnotation(ApiService.class);
        if (apiService == null) {
            throw new PlatformException("{%s}接口上缺少必须的注解{@%s}", serviceInterfaceClass.getSimpleName(), ApiService.class.getSimpleName());
        }
        ApiServiceOperation apiServiceOperation = method.getAnnotation(ApiServiceOperation.class);
        if (apiServiceOperation == null) {
            throw new PlatformException("{接口方法{%s}上缺少必须的注解{%s}", method.getName(), ApiServiceOperation.class.getSimpleName());
        }

        // 根节点接口与子节点接口是否对应判断
        if (!apiService.value().equals(apiServiceOperation.value().getRootUrl())) {
            throw new PlatformException("{%s}接口类注解的参数{%s}与接口方法{%s}的注解参数{%s}不一致",
                    serviceInterfaceClass.getSimpleName(), apiService.value().name(),
                    method.getName(), apiServiceOperation.value().getRootUrl().name());
        }
        // 请求地址
        String fullUrl = apiService.value().getRootUrl() + apiServiceOperation.value().getApi();

        log.info("请求服务接口-[{}],请求参数值-[{}] - 请求接口地址[{}]", method.getName(), Arrays.toString(args), fullUrl);

        // 格式化请求参数
        Map<String, Object> params = ObjectToJsonMap(args);
        String result;
        if (RequestMethod.GET.equals(apiServiceOperation.method())) {
            result = HttpClient.create().get(fullUrl, null, params);
        } else {
            result = HttpClient.create().post(fullUrl, null, params);
        }
        log.info("接口[{}]响应结果 - {}", fullUrl, result);

        // 反序列化响应结果
        return JSON.toJavaObject(JSON.parseObject(result), method.getReturnType());
    }

    /**
     * 将JavaBean请求参数，使用fastjson转换为map
     *
     * @param params 请求参数
     * @return
     */
    private Map<String, Object> ObjectToJsonMap(Object[] params) {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        if (params == null || params.length == 0) {
            return paramMap;
        }
        Object param = params[0];
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(param));
        paramMap.putAll(jsonObject);
        return paramMap;
    }
}
