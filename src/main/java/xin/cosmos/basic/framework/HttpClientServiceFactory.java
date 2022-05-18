package xin.cosmos.basic.framework;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 通过Bean工厂创建JDK动态代理实例
 *
 * @param <T>
 */
public class HttpClientServiceFactory<T> implements FactoryBean<T> {

    private Class<T> interfaceType;

    public HttpClientServiceFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() {
        // 创建接口对应的代理实例，注入到Spring容器中
        InvocationHandler handler = new JdkDynamicHttpClientProxy<>(interfaceType);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, handler);
    }


    @Override
    public Class<?> getObjectType() {
        return this.interfaceType;
    }

    /**
     * 单例模式
     *
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
