package xin.cosmos.basic.framework.annotation;

import xin.cosmos.basic.framework.enums.ApiSubUrl;
import xin.cosmos.basic.framework.enums.RequestMethod;

import java.lang.annotation.*;

/**
 * api服务接口描述 - 仅用在服务接口类的方法上，标明第三方服务平台根具体接口描述信息
 * <p>
 * 接口请求方式默认为 Get请求
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiServiceOperation {

    /**
     * 第三方服务接口-具体接口
     *
     * @return
     */
    ApiSubUrl value();

    /**
     * 接口请求方式
     *
     * @return
     */
    RequestMethod method() default RequestMethod.GET;
}
