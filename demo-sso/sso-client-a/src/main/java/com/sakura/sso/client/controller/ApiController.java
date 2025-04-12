package com.sakura.sso.client.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {

    /**
     * 公开接口，不需要认证
     */
    @GetMapping("/api/public/info")
    public Map<String, Object> publicInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个公开接口，不需要认证");
        result.put("time", System.currentTimeMillis());
        return result;
    }

    /**
     * 安全接口，需要认证
     */
    @GetMapping("/api/secure/userinfo")
    public Map<String, Object> secureUserInfo(HttpServletRequest request) {
        // 从请求属性中获取用户名（拦截器中设置）
        String username = (String) request.getAttribute("username");
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个受保护的接口，需要认证");
        result.put("username", username);
        result.put("time", System.currentTimeMillis());
        return result;
    }
} 