package xin.cosmos.basic.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.exception.BusinessException;
import xin.cosmos.basic.exception.PlatformException;

import java.util.List;

/**
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String DEFAULT_ERROR_CODE = "error";
    private static final String DEFAULT_ERROR_MSG = "业务繁忙,请稍后再试";

    public static final String DEFAULT_PARAM_ERROR_CODE = "param_error";
    private static final String DEFAULT_PARAM_ERROR_MSG = "参数不合法";

    /**
     * 全局异常捕捉处理
     *
     * @param ex 异常信息
     * @return 返回异常结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> errorHandler(Exception ex) {
        log.error("全局异常:", ex);
        return ResultVO.failed(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG);
    }

    @ExceptionHandler(value = {BusinessException.class, PlatformException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> handleBusinessException(PlatformException ex) {
        log.error("异常错误:{}-{}-{}", ex.getResultCode().name().toLowerCase(), ex.getMessage(), ex);
        return ResultVO.failed(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> illegalArgumentException(IllegalArgumentException ex) {
        log.error("全局异常:", ex);
        return ResultVO.failed(DEFAULT_PARAM_ERROR_CODE, DEFAULT_PARAM_ERROR_MSG);
    }

    @ExceptionHandler({BindException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> bindException(BindException ex) {
        log.error("全局异常:", ex);
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            return ResultVO.failed(getErrorMsg(objectErrors));
        }
        return new ResultVO<>(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG);
    }

    private String getErrorMsg(List<ObjectError> objectErrors) {
        StringBuilder msgBuilder = new StringBuilder();
        for (ObjectError objectError : objectErrors) {
            msgBuilder.append(objectError.getDefaultMessage()).append(",");
        }
        String errorMessage = msgBuilder.toString();
        if (errorMessage.length() > 1) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
        }
        return errorMessage;
    }
}
