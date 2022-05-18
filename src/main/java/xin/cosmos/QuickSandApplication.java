package xin.cosmos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import xin.cosmos.basic.helper.ContextHolder;

@SpringBootApplication
public class QuickSandApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(QuickSandApplication.class, args);
        ContextHolder.initApplicationContext(applicationContext);
    }
}
