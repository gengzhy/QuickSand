package xin.cosmos.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.dict.bill.disclosure.BillAcceptanceMetaType;

import java.util.*;

@Api(tags = "首页-Home")
@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String toHomePage(ModelMap map) {
        map.addAttribute("title", "欢迎来到首页");
        return "index";
    }

    @ApiOperation(value = "票据承兑人信用披露信息-元数据类类型列表")
    @PostMapping(value = "getBusinessTypes")
    @ResponseBody
    public ResultVO<List<Map<String, String>>> getBusinessTypes() {
        List<Map<String, String>> businessTypes = new ArrayList<>();
        for (BillAcceptanceMetaType type : BillAcceptanceMetaType.values()) {
            businessTypes.add(new HashMap() {{
                put("name", type.getDesc());
                put("value", type.name());
            }});
        }
        return ResultVO.success(businessTypes);
    }
}
