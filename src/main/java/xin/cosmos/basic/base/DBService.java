package xin.cosmos.basic.base;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Component;

/**
 * 数据库工具类
 */
@Component
public class DBService {
    /**
     * 分隔符
     */
    final static String SEPARATOR_DOT = ".";
    private final JdbcTemplate jdbcTemplate;

    public DBService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 清空数据库表数据
     *
     * @param dbName    数据库名
     * @param tableName 表名
     */
    public void clearTable(String dbName, String tableName) {
        final String command = "truncate " + dbName + SEPARATOR_DOT + tableName;
        jdbcTemplate.execute((StatementCallback<Boolean>) stmt -> stmt.execute(command));
    }
}
