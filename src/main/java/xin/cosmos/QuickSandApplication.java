package xin.cosmos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class QuickSandApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickSandApplication.class, args);
    }

    @RestController
    class Demo {

        @GetMapping("/hello")
        public String hello() {
            return "hello world";
        }
    }

}
