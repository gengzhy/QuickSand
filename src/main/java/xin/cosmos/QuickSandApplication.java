package xin.cosmos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xin.cosmos.basic.helper.ContextHolder;

/**
 * 启动类
 *
 * @author geng
 */
@EnableTransactionManagement
@SpringBootApplication
public class QuickSandApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(QuickSandApplication.class, args);
        ContextHolder.initApplicationContext(applicationContext);
    }
}
