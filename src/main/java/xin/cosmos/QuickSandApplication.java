package xin.cosmos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 *
 * @author geng
 */
@EnableTransactionManagement
@SpringBootApplication
public class QuickSandApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickSandApplication.class, args);
    }
}
