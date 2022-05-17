package xin.cosmos;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class QuickSandApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickSandApplication.class, args);
    }

    @Api(tags = "测试Controller")
    @RestController
    class DemoController {

        @ApiOperation(value = "hello 测试接口")
        @GetMapping("/hello")
        public String hello() {
            return "hello world";
        }
    }

}
