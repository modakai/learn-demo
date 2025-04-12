package com.sakura.sso.server.service;

import cn.hutool.crypto.SecureUtil;
import com.sakura.sso.server.model.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    // 用户信息存储，实际项目中应该使用数据库
    private final Map<String, String> userPasswordMap = new ConcurrentHashMap<>();
    // token与用户名映射关系
    private final Map<String, String> tokenUserMap = new ConcurrentHashMap<>();
    // 用户名与token映射关系
    private final Map<String, String> userTokenMap = new ConcurrentHashMap<>();

    public TokenService() {
        // 初始化测试用户
        userPasswordMap.put("admin", SecureUtil.md5("123456"));
        userPasswordMap.put("user", SecureUtil.md5("123456"));
    }

    /**
     * 登录校验
     */
    public UserInfo login(String username, String password) {
        // 验证用户名密码
        String storedPassword = userPasswordMap.get(username);
        if (storedPassword != null && storedPassword.equals(SecureUtil.md5(password))) {
            // 生成token
            String token = UUID.randomUUID().toString().replace("-", "");
            
            // 如果用户已经登录，先移除旧token
            String oldToken = userTokenMap.get(username);
            if (oldToken != null) {
                tokenUserMap.remove(oldToken);
            }
            
            // 保存新token映射
            tokenUserMap.put(token, username);
            userTokenMap.put(username, token);
            
            return new UserInfo(username, null, token);
        }
        return null;
    }

    /**
     * 校验token是否有效
     */
    public boolean validateToken(String token) {
        return tokenUserMap.containsKey(token);
    }

    /**
     * 根据token获取用户名
     */
    public String getUsernameByToken(String token) {
        return tokenUserMap.get(token);
    }

    /**
     * 登出
     */
    public void logout(String token) {
        String username = tokenUserMap.get(token);
        if (username != null) {
            tokenUserMap.remove(token);
            userTokenMap.remove(username);
        }
    }
} 