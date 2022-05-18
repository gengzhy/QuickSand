package xin.cosmos.basic.framework;

import lombok.extern.slf4j.Slf4j;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.httpclient.HttpClient;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

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
     * 真正执行动态代理调用的方法
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
        Annotation[] annotations = serviceApiInterface.getDeclaredAnnotations();
        Method declaredMethod = serviceApiInterface.getDeclaredMethod(method.getName(), method.getParameterTypes());
        Parameter[] parameters = declaredMethod.getParameters();

        log.info("请求服务接口类-[{}],请求服务接口方法-[{}],请求参数值-[{}]", serviceApiInterface.getName(), method.getName(), Arrays.toString(args));
        // TODO 真正执行代理功能的地方
        String result = HttpClient.create().get("http://liusha.xyz", null, null);
        log.info("执行接口响应结果：{}", result);
        return ResultVO.success(result);
    }
}
