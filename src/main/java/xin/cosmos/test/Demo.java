package xin.cosmos.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Demo {

    public static void main(String[] args) {
        log.info("业务开始");
        AtomicInteger i = new AtomicInteger();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            int count = i.incrementAndGet();
            log.info("任务执行中...当前执行到第{}步", count);
            if (count >= 10) {
                service.shutdown();
            }
        }, 1000L, 3000L, TimeUnit.MILLISECONDS);
        log.info("业务结束");
    }

    void handle(String msg, User user) {
        log.info("主线程【{}】开始执行：{}, {}", Thread.currentThread().getName(), msg, user);
        new Thread(() -> {
            int i = 0;
            while (i < 10) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.error("子线【{}】程程开始执行：{}, {}", Thread.currentThread().getName(), msg, user);
                i++;
            }
        }).start();
        log.info("主线程【{}】执行结束：{}, {}", Thread.currentThread().getName(), msg, user);
    }

}
@Data
@AllArgsConstructor
class User {
    String name;
}
