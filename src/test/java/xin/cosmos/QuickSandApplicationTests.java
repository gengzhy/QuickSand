package xin.cosmos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import xin.cosmos.basic.base.RedisService;
import xin.cosmos.basic.ssh2.Ssh2Service;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@SpringBootTest
class QuickSandApplicationTests {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Test
    void contextLoads() {
        System.out.println(dataSource.getClass());
        Map<String, Object> map = jdbcTemplate.queryForMap("select now() from dual");
        map.forEach((k, v) -> System.out.println(v));
    }

    @Autowired
    private Ssh2Service ssh2Service;
    @Test
    public void testShh2() {
        String command = "pwd";
        ssh2Service.execute(command);
    }

    @Autowired
    private RedisService redisService;
    @Test
    public void testBackUp() {
        redisService.syncSave();
    }

}
