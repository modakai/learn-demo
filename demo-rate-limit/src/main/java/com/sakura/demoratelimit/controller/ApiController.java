package com.sakura.demoratelimit.controller;

import com.sakura.demoratelimit.annotation.RateLimit;
import com.sakura.demoratelimit.modal.ResponseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ApiController {

    // 应用注解，例如：1 秒内最多允许 3 次请求
    @RateLimit(key = "api:index", maxRequests = 3, windowSize = 1, timeUnit = TimeUnit.SECONDS, message = "访问太快了，喝杯茶休息一下吧！")
    @GetMapping("/api/index")
    public ResponseVo index() {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode(200);
        responseVo.setMsg("success");
        return responseVo;
    }
}
