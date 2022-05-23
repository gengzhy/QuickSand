package xin.cosmos.basic.framework.annotation;

import xin.cosmos.basic.framework.enums.ApiRootUrl;

import java.lang.annotation.*;

/**
 * api服务接口描述 - 仅用在服务接口类上，标明第三方服务平台根接口描述信息
 * <p>
 *
 * @author geng
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiSupport {
    /**
     * 第三方服务接口
     *
     * @return
     */
    ApiRootUrl value();
}
