package xin.cosmos.basic.filter;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域处理
 *
 * @author geng
 */
@Component
@ServletComponentScan(value = {"xin.cosmos.controller"})
@WebFilter(urlPatterns = "/*", filterName = "corsFilter")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // 允许哪些Origin发起跨域请求
        String originHeader = request.getHeader("Allowed-Origin");
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许请求的方法
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
        // 多少秒内，不需要再发送预检验请求
        response.setHeader("Access-Control-Max-Age", "3600");
        // 表明它允许跨域请求包含xxx头
        response.setHeader("Access-Control-Allow-Headers", "x-auth-token,Origin,Access-Token,X-Requested-With,Content-Type, Accept,multipart/form-data,Authorization");
        //是否允许浏览器携带用户身份信息（cookie）
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // localhost->localhost的跨域请求
        response.setHeader("Access-Control-Allow-Private-Network", "true");
        chain.doFilter(req, res);
    }
}