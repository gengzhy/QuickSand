package xin.cosmos.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "首页-Home")
@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String toHomePage() {
        return "index";
    }

    @ApiOperation(value = "票据承兑人信用披露信息管理")
    @GetMapping(value = "/bill/info")
    public String toBillInfoPage() {
        return "bill/index";
    }
}
