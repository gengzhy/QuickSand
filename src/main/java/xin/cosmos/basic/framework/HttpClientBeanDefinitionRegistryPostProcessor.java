package xin.cosmos.basic.framework;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

public class HttpClientBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 1.扫描包，继承ClassPathBeanDefinitionScanner，重写isCandidateComponent()方法
        ClassPathBeanDefinitionScanner httpClientClassPathBeanDefinitionScanner = new HttpClientClassPathBeanDefinitionScanner(registry);

        // 循环注册Bean
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
