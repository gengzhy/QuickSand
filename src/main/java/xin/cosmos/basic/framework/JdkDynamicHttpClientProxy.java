package xin.cosmos.basic.framework;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * httpclient对应服务接口的动态代理，基于jdk动态代理
 *
 * @param <T>
 */
public class JdkDynamicHttpClientProxy<T> implements InvocationHandler {
    private Class<T> interfaceType;

    public JdkDynamicHttpClientProxy(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
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

        System.out.println("调用前，参数：{}" + Arrays.toString(args));
        // TODO 真正执行代理功能的地方
        //这里可以得到参数数组和方法等，可以通过反射，注解等，进行结果集的处理
        //mybatis就是在这里获取参数和相关注解，然后根据返回值类型，进行结果集的转换
        Object result = JSON.toJSONString(args);
        System.out.println("调用后，结果：{}" + result);
        return null;
    }
}
