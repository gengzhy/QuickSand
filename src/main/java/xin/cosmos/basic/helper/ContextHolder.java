package xin.cosmos.basic.helper;

import org.springframework.context.ApplicationContext;
import xin.cosmos.basic.exception.PlatformException;

/**
 * application上下文bean实例帮助类
 *
 * @author geng
 */
public class ContextHolder {
    private static ApplicationContext applicationContext;

    private ContextHolder() {
        throw new PlatformException("非法操作");
    }

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
