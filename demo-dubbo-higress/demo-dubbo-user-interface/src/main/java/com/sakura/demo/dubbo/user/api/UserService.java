package com.sakura.demo.dubbo.user.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/user")
public interface UserService {

    @GetMapping("/sayHello")
    public String sayHello(@RequestParam("name") String name, @RequestHeader("authorization") String authHeader);

    @GetMapping("/getInfo")
    public String getInfo(@RequestParam("name") String name);
}
