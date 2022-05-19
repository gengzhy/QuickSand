package xin.cosmos.basic.framework;

import org.springframework.beans.factory.FactoryBean;
import xin.cosmos.basic.framework.annotation.HttpClientScannerPackage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 通过Bean工厂创建JDK动态代理实例
 *
 * @param <T>
 */
@HttpClientScannerPackage(packages = {"xin.cosmos.basic.api"})
public class HttpClientServiceFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> serviceInterfaceClass;

    public HttpClientServiceFactoryBean(Class<T> serviceInterfaceClass) {
        this.serviceInterfaceClass = serviceInterfaceClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() {
        // 创建接口对应的代理实例，注入到Spring容器中
        InvocationHandler handler = new JdkDynamicHttpClientProxy<>(serviceInterfaceClass);
        return (T) Proxy.newProxyInstance(serviceInterfaceClass.getClassLoader(), new Class[]{serviceInterfaceClass}, handler);
    }


    @Override
    public Class<?> getObjectType() {
        return this.serviceInterfaceClass;
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
