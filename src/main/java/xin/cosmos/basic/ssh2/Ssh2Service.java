package xin.cosmos.basic.ssh2;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.exception.PlatformException;
import xin.cosmos.basic.util.ObjectsUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Linux ssh2 shell命令行执行工具类
 *
 * @author geng
 */
@Slf4j
@Component
public class Ssh2Service {

    @Value("${ssh2.remote.ip:127.0.0.1}")
    private String ip;
    @Value("${ssh2.remote.port:22}")
    private int port;
    @Value("${ssh2.remote.username:root}")
    private String username;
    @Value("${ssh2.remote.password:root}")
    private String password;

    /**
     * 初始化连接
     */
    private Connection init() {
        Connection connection = null;
        try {
            connection = new Connection(ip, port);
            // 建立连接
            connection.connect();
            // 进行认证
            connection.authenticateWithPassword(username, password);
        } catch (IOException e) {
            connection.close();
            throw new PlatformException("主机认证失败: %s", e.getMessage());
        }
        return connection;
    }

    /**
     * 执行远程命令.
     *
     * @param command shell脚本命令
     */
    public ResultVO<String> execute(String command) {
        String result;
        Connection connection = null;
        Session session = null;
        try {
            connection = init();
            //打开一个会话
            session = connection.openSession();
            //执行命令
            session.execCommand(command);
            // 得到标准输出结果
            result = processStdout(session.getStdout());
            //如果为得到标准输出为空，说明脚本执行出错了
            if (ObjectsUtil.isNull(result)) {
                result = processStdout(session.getStderr());
                log.error("执行命令[{}]失败, error-{}", command, result);
                return ResultVO.failed("执行命令[" + command + "]失败, error[" + result + "]");
            } else {
                log.info("执行命令[{}]成功, result-{}", command, result);
                return ResultVO.success("执行成功", result);
            }
        } catch (IOException e) {
            log.error("执行命令失败,链接conn:{}, 执行的命令:{},error:{}", connection, command, e.getMessage());
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                log.info("登录连接失败:{}", e.getMessage());
            }
        }
        return ResultVO.success("执行失败");
    }

    /**
     * 执行命令返回集.
     */
    private String processStdout(InputStream in) {
        InputStream stdout = new StreamGobbler(in);
        StringBuilder buffer = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (IOException e) {
            log.error("解析脚本出错: {}", e.getMessage());
        }
        return buffer.toString();
    }

}
