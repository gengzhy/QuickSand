package xin.cosmos;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        @PostMapping("/hello")
        public User hello(@RequestBody User user) {
            return user;
        }
    }

    @Data
    @ApiModel(description = "用户参数")
    static class User {
        @ApiModelProperty(value = "用户名", required = true, example = "ian")
        private String username;

        @ApiModelProperty(value = "年龄", required = true, allowableValues = "[1,100]")
        private Integer age;
    }

}
