package com.sakura.sso.client.interceptor;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${sso.server.url}")
    private String ssoServerUrl;

    @Value("${sso.server.verifyPath}")
    private String verifyPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\":\"未授权，请先登录\"}");
            return false;
        }

        // 去除Bearer前缀（如果有）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 调用SSO服务器验证token
        String verifyUrl = ssoServerUrl + verifyPath + "?token=" + token;
        try {
            String result = HttpUtil.get(verifyUrl);
            JSONObject jsonResult = JSONUtil.parseObj(result);
            
            if (jsonResult.getBool("valid", false)) {
                // 将用户信息放入请求属性中
                request.setAttribute("username", jsonResult.getStr("username"));
                return true;
            } else {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"message\":\"token无效或已过期\"}");
                return false;
            }
        } catch (Exception e) {
            log.error("验证token失败", e);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"验证token时发生错误\"}");
            return false;
        }
    }
} 