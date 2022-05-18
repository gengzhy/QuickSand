package xin.cosmos.basic.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import xin.cosmos.basic.framework.annotation.HttpClientScannerPackage;

import java.util.Arrays;
import java.util.Set;

/**
 * 包扫描器
 */

@Slf4j
public class HttpClientClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner implements InitializingBean {
    /**
     * 通过Bean工厂创建JDK动态代理FactoryBean
     */
    private Class<? extends HttpClientServiceFactoryBean> serviceFactoryBeanClass = HttpClientServiceFactoryBean.class;

    /**
     * 接口扫描包
     */
    private String basePackage;

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getBasePackage() {
        return this.basePackage;
    }

    public HttpClientClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);

        // 设置扫描包
        this.setBasePackage();
    }

    /**
     * 设置接口扫描包
     */
    private void setBasePackage() {
        if (serviceFactoryBeanClass.isAnnotationPresent(HttpClientScannerPackage.class)) {
            HttpClientScannerPackage factoryAnnotation = serviceFactoryBeanClass.getAnnotation(HttpClientScannerPackage.class);
            String[] packages = factoryAnnotation.packages();
            if (packages != null && packages.length > 0) {
                //todo 暂时仅支持一个包扫描，后续改进支持多包扫描
                final String scanPackage = packages[0];
                this.setBasePackage(scanPackage);
            }
        } else {
            this.afterPropertiesSet();
        }
    }

    /**
     * 包扫描注册BeanDefinition
     */
    protected void scanApiPackages() {
        this.doScan(this.getBasePackage());
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        //TODo 构建BeanDefinition
        if (beanDefinitionHolders.isEmpty()) {
            log.warn("没有可以注册的 HttpClientServiceFactoryBean Interface: '" + Arrays.toString(basePackages) + "' package. 请在HttpClientServiceFactoryBean中使用注解@HttpClientScannerPackage指定接口的扫描包路径");
        } else {
            this.processBeanDefinitions(beanDefinitionHolders);
        }
        return beanDefinitionHolders;

    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            log.info("Creating ServiceFactoryBeanClass with name '" + holder.getBeanName() + "' and '" + beanClassName + "' Service Interface");

            // 将接口替换为HttpClientServiceFactoryBean注册为BeanDefinition
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(this.serviceFactoryBeanClass);
        }
    }

    /**
     * 仅扫描接口
     *
     * @param beanDefinition
     * @return
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) {
        return metadataReader.getClassMetadata().isInterface() && metadataReader.getClassMetadata().isIndependent();
    }

    @Override
    public void afterPropertiesSet() {
        if (this.basePackage == null) {
            throw new IllegalArgumentException("必须在HttpClientServiceFactoryBean.class上使用注解@HttpClientScannerPackage指定接口扫描包.");
        }
    }
}
