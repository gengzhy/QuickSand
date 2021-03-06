package xin.cosmos.basic.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import xin.cosmos.basic.exception.BusinessException;
import xin.cosmos.basic.exception.PlatformException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * 系统全局异常处理
 *
 * @author geng
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    public static final String DEFAULT_ERROR_CODE = "ERROR";
    private static final String DEFAULT_ERROR_MSG = "业务繁忙,请稍后再试";


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView handleException(HttpServletRequest request, HttpServletResponse response,
                                        Exception ex, HandlerMethod handle) {
        ModelAndView modelAndView;
        // JSON请求
        if (handle.getBean().getClass().isAnnotationPresent(RestController.class) ||
                handle.hasMethodAnnotation(ResponseBody.class)) {
            modelAndView = new ModelAndView(new MappingJackson2JsonView());
            this.handleSpecialException(ex, handle, modelAndView);
            modelAndView.addObject("data", null);
        }
        // 普通请求
        else {
            modelAndView = new ModelAndView();
            modelAndView.setViewName("error/error");
            this.handleSpecialException(ex, handle, modelAndView);
            PrintWriter writer = new PrintWriter(new StringWriter());
            ex.printStackTrace(writer);
        }
        return modelAndView;
    }

    private void handleSpecialException(Exception e, HandlerMethod handle, ModelAndView modelAndView) {
        log.warn("[请求异常接口信息]{}#{}{}", handle.getBean().getClass().getName(), handle.getMethod().getName(), paramTypes(handle));

        if (e instanceof BusinessException) {
            BusinessException ex = (BusinessException) e;
            log.error("[业务级别异常]{} - {} \n[call stack]=>\n", ex.getResultCode().name(), ex.getMessage(), e);
            modelAndView.addObject("code", ex.getResultCode());
            modelAndView.addObject("message", ex.getMessage());
        } else if (e instanceof PlatformException) {
            PlatformException ex = (PlatformException) e;
            log.error("[平台级别异常]{} - {} \n[call stack]=>\n", ex.getResultCode().name(), ex.getMessage(), e);
            modelAndView.addObject("code", ex.getResultCode());
            modelAndView.addObject("message", ex.getMessage());
        } else {
            log.error("[全局级别异常]{} \n[call stack]=>\n", e.getMessage(), e);
            modelAndView.addObject("code", DEFAULT_ERROR_CODE);
            modelAndView.addObject("message", DEFAULT_ERROR_MSG);
        }
    }

    private String paramTypes(HandlerMethod handle) {
        StringBuilder builder = new StringBuilder("(");
        Arrays.stream(handle.getMethod().getParameterTypes()).forEach(e -> builder.append(e.getName()).append(", "));
        int lastIndex;
        if ((lastIndex = builder.lastIndexOf(", ")) != -1) {
            builder.replace(lastIndex, builder.length(), "");
        }
        return builder.append(")").toString();
    }

    private String trance(StackTraceElement[] traceElements) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(traceElements).forEach(e -> builder.append(e.toString()).append("\n"));
        return builder.toString();
    }
}
