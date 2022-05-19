package xin.cosmos.basic.handler;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.cosmos.basic.define.ResultVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ServletErrorController extends BasicErrorController {

    public ServletErrorController(ErrorAttributes errorProperties) {
        super(errorProperties, new ErrorProperties());
    }

    /**
     * Servlet请求异常处理器
     *
     * @param request
     * @return
     */
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        final Map<String, Object> body = getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION));
        String error = body.get("path") + " " + body.get("error");
        return new ResponseEntity<>(JSONObject.parseObject(JSONObject.toJSONString(ResultVO.failed(error))), HttpStatus.OK);
    }
}
