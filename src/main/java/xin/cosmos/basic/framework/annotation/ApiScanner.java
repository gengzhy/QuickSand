package xin.cosmos.basic.framework.annotation;


import java.lang.annotation.*;

/**
 * HttpClient接口包扫描注解
 * <p>
 * 强烈建议：扫描的包内除了业务接口外，不要掺杂其他无关类，避免未知的异常发生
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
