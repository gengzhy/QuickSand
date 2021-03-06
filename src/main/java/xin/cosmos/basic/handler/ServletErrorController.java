package xin.cosmos.basic.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import xin.cosmos.basic.define.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求未到达Servlet时，使用@ControllerAdvice无法拦截，如404
 * 使用该异常信息处理
 *
 * @author geng
 */
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ServletErrorController extends BasicErrorController {

    @Value("${spring.application.name:quick-sand}")
    private String context;

    public ServletErrorController(ErrorAttributes errorProperties) {
        super(errorProperties, new ErrorProperties());
    }

    @Override
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML));
        Map<String, Object> map = new HashMap<>();
        map.put("title", context + " - " + status.getReasonPhrase() + "page");
        map.put("code", model.get("status"));
        map.put("message", model.get("error"));
        response.setStatus(status.value());
        log.error("{}", model);
        return new ModelAndView("error/error", map, HttpStatus.OK);
    }

    /**
     * Servlet请求异常处理器
     *
     * @param request
     * @return
     */
    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        final Map<String, Object> body = getErrorAttributes(request, errorAttributeOptions());
        String error = body.get("status") + " " + body.get("error");
        log.error("{}", body);
        return new ResponseEntity<>(JSON.parseObject(JSON.toJSONString(ResultVO.failed(error))), HttpStatus.OK);
    }

    private ErrorAttributeOptions errorAttributeOptions() {
        return ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION,
                ErrorAttributeOptions.Include.BINDING_ERRORS,
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.STACK_TRACE);
    }
}
