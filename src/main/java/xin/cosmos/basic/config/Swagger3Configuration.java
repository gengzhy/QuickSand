package xin.cosmos.basic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger3配置类
 */
@Slf4j
@Configuration
@EnableOpenApi
public class Swagger3Configuration {
    /**
     * swagger3 静态资源路径
     */
    public final static String[] SWAGGER_PATH = {"/doc.html**", "/webjars/**", "/img/**"};

    /**
     * swagger3 扫描的controller
     */
    public final static String CONTROLLER_PACKAGE = "xin.cosmos.controller";

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:qs}")
    private String rootPath;

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.OAS_30);
        docket.apiInfo(apiInfo())
                .groupName("api")
                .select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE))
                .paths(PathSelectors.any());
        log.info("Swagger3 - 浏览器根路径访问地址: http://localhost:{}{}/doc.html", port, rootPath);
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("QuickSand")
                .description("QuickSand SpringBoot Project")
                .version("1.0")
                .contact(new Contact("Geng Zhongyi", "https://cosmos.xin", "gengzyy@gmail.com"))
                .build();
    }
}
