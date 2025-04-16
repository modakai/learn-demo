package com.sakura.idempotent.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakura.idempotent.annotation.Idempotent;
import com.sakura.idempotent.service.IdempotentService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Aspect
@Component
public class IdempotentAspect {
    @Autowired
    private IdempotentService idempotentService;

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {

        // 获取请求参数
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        String key = generateKey(request, joinPoint, idempotent);

        // 检查幂等性
        if (!idempotentService.check(key, idempotent.expireSeconds())) {
            // 重复请求，抛出异常或返回错误信息
            throw new RuntimeException("重复请求，请稍后再试");
        }

        // 不是重复请求，执行原方法
        return joinPoint.proceed();
    }

    /**
     * 生成幂等键
     */
    private String generateKey(HttpServletRequest request, ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        StringBuilder keyBuilder = new StringBuilder(idempotent.prefix());

        // 获取类名和方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        keyBuilder.append(className).append(".").append(methodName).append(":");
        switch (idempotent.keySource()) {
            // 从使用令牌来
            case TOKEN -> keyBuilder.append(request.getHeader("Authorization"));
            case PARAMETER -> {
                String paramValue = request.getParameter(idempotent.keyName());
                if (StringUtils.hasText(paramValue)) {
                    keyBuilder.append(paramValue);
                } else {
                    // 获取请求参数的MD5
                    keyBuilder.append(generateMD5FromRequest(request, joinPoint));
                }
            }
            case HEADER -> keyBuilder.append(request.getHeader(idempotent.keyName()));
        }
        return keyBuilder.toString();
    }

    /**
     * 生成请求参数的MD5值
     */
    private String generateMD5FromRequest(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        // 获取方法参数
        Object[] args = joinPoint.getArgs();

        // 使用JSON序列化参数，然后生成MD5
        try {
            String paramJson = new ObjectMapper().writeValueAsString(args);
            return DigestUtils.md5DigestAsHex(paramJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return UUID.randomUUID().toString();
        }
    }

}
