package xin.cosmos.basic.framework.annotation;


import java.lang.annotation.*;

/**
 * HttpClient接口包扫描注解
 *
 * @author geng
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiScanner {

    /**
     * 需要扫描的接口的包路径
     *
     * @return
     */
    String[] packages();
}
