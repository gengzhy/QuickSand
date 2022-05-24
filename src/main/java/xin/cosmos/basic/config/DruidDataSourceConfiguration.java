package xin.cosmos.basic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xin.cosmos.basic.config.props.DruidDataSourceProperties;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * druid 数据源
 */
@Slf4j
@Configuration
public class DruidDataSourceConfiguration {

    @SneakyThrows
    @Bean
    public DataSource druidDataSource(DruidDataSourceProperties prop) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(prop.getDriverClassName());
        dataSource.setUrl(prop.getUrl());;
        dataSource.setUsername(prop.getUsername());
        dataSource.setPassword(prop.getPassword());
        dataSource.setInitialSize(prop.getInitialSize());
        dataSource.setMinIdle(prop.getMinIdle());
        dataSource.setMaxActive(prop.getMaxActive());
        dataSource.setMaxWait(prop.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(prop.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(prop.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(prop.getValidationQuery());
        dataSource.setTestWhileIdle(prop.getTestWhileIdle());
        dataSource.setTestOnBorrow(prop.getTestOnBorrow());
        dataSource.setTestOnReturn(prop.getTestOnReturn());
        dataSource.setPoolPreparedStatements(prop.getPoolPreparedStatements());
        dataSource.setFilters(prop.getFilters());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(prop.getMaxPoolPreparedStatementPerConnectionSize());
        dataSource.setUseGlobalDataSourceStat(prop.getUseGlobalDataSourceStat());
        dataSource.setConnectionProperties(prop.getConnectionProperties());
        return dataSource;
    }

    /**
     * 配置 Druid 监控管理后台的Servlet
     * <p>
     * 内置 Servlet 容器时没有web.xml文件，所以使用 Spring Boot 的注册 Servlet 方式
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 这些参数可以在 com.alibaba.druid.support.http.StatViewServlet 的父类 com.alibaba.druid.support.http.ResourceServlet 中找到
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", "admin");
        initParams.put("loginPassword", "admin@520");
        initParams.put("allow", "127.0.0.1");  //默认就是允许所有访问
        // initParams.put("deny", "ip"); //deny：Druid 后台拒绝谁访问，表示禁止此ip访问
        initParams.put("resetEnable", "false"); //是否可以重置数据
        bean.setInitParameters(initParams);
        return bean;
    }

    /**
     * 配置一个web监控的filter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean webStatFilter() {
        //创建过滤器
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();
        // 或略过滤的形式
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        bean.setInitParameters(initParams);
        //设置过滤路径
        bean.setUrlPatterns(Collections.singletonList("/*"));

        return bean;
    }
}
