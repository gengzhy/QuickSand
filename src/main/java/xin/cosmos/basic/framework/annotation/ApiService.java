package xin.cosmos.basic.framework.annotation;

import xin.cosmos.basic.framework.enums.ApiRootUrl;
import xin.cosmos.basic.framework.enums.RequestMethod;

import java.lang.annotation.*;

/**
 * api服务接口描述 - 仅用在服务接口类上，标明第三方服务平台根接口描述信息
 * <p>
 * 接口请求方式默认为 Get请求
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiService {
    /**
     * 第三方服务接口
     *
     * @return
     */
    ApiRootUrl value();

    /**
     * 接口请求方式
     *
     * @return
     */
    RequestMethod method() default RequestMethod.GET;
}
