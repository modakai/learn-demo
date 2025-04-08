package com.sakura.demoratelimit.controller;

import com.sakura.demoratelimit.modal.ResponseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/index")
    public ResponseVo index() {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode(200);
        responseVo.setMsg("success");
        return responseVo;
    }
}
