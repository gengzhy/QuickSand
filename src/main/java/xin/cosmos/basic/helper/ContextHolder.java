package xin.cosmos.basic.helper;

import org.springframework.context.ApplicationContext;

/**
 * application上下文bean实例帮助类
 */
public class ContextHolder {
    private static ApplicationContext applicationContext;

    public static void initApplicationContext(ApplicationContext applicationContext) {
        ContextHolder.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> requireType) {
        return applicationContext.getBean(requireType);
    }

    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }
}
