package com.sakura.sso.server.controller;

import com.sakura.sso.server.model.UserInfo;
import com.sakura.sso.server.service.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8081"}, allowCredentials = "true")
public class SsoController {

    private final TokenService tokenService;

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 登录处理
     */
    @PostMapping("/login")
    public void login(@RequestParam("username") String username,
                     @RequestParam("password") String password,
                     @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                     HttpServletResponse response) throws IOException {
        
        UserInfo userInfo = tokenService.login(username, password);
        
        if (userInfo != null) {
            // 登录成功，重定向到客户端
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                // 将token追加到重定向URL
                String redirectWithToken = redirectUrl + (redirectUrl.contains("?") ? "&" : "?") + "token=" + userInfo.getToken();
                response.sendRedirect(redirectWithToken);
            } else {
                // 如果没有重定向URL，显示登录成功页面或返回JSON
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":true,\"token\":\"" + userInfo.getToken() + "\"}");
            }
        } else {
            // 登录失败，返回登录页面
            response.sendRedirect("/login?error");
        }
    }

    /**
     * 验证token
     */
    @GetMapping("/verify")
    @ResponseBody
    public Map<String, Object> verifyToken(@RequestParam("token") String token) {
        Map<String, Object> result = new HashMap<>();
        boolean valid = tokenService.validateToken(token);
        result.put("valid", valid);
        
        if (valid) {
            result.put("username", tokenService.getUsernameByToken(token));
        }
        
        return result;
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    @ResponseBody
    public Map<String, Object> logout(@RequestParam("token") String token) {
        Map<String, Object> result = new HashMap<>();
        tokenService.logout(token);
        result.put("success", true);
        return result;
    }
} 