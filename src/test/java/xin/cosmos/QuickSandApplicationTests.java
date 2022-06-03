package xin.cosmos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
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
        Map<String, Object> map = jdbcTemplate.queryForMap("select 1 from dual");
        map.forEach((k, v) -> System.out.println(v));
    }

    @Autowired
    private Ssh2Service ssh2Service;
    @Test
    public void testShh2() {
        String command = "cd /soft/webJar;pwd";
        log.info(ssh2Service.execute(command).toString());
    }

}
