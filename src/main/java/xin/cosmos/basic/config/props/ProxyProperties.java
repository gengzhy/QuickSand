package xin.cosmos.basic.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代理属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "proxy")
public class ProxyProperties {
    /**
     * 是否启用远程代理[true/false]
     */
    private boolean enable;
    /**
     * 代理主机
     */
    private String host;
    /**
     * 代理端口
     */
    private int port;
}
