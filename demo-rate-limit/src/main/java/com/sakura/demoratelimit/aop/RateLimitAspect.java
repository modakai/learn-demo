package com.sakura.demoratelimit.aop;

import com.sakura.demoratelimit.annotation.RateLimit;
import com.sakura.demoratelimit.modal.ResponseVo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 */
@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class); // lua返回值类型
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/ratelimit_sliding_window.lua")));
        log.info("redis lua 限流脚本加载成功");
    }

    // 定义切点，拦截所有标记了 @RateLimit 注解的方法
    @Pointcut("@annotation(com.sakura.demoratelimit.annotation.RateLimit)")
    public void rateLimitPointcut() {
    }

    @Around("rateLimitPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        if (rateLimit == null) {
            // 理论上不会发生，因为切点已经指定了注解
            return joinPoint.proceed();
        }

        // 获取key
        String baseKey = rateLimit.key();
        if (baseKey == null || baseKey.isEmpty()) {
            // 如果注解没有指定 key，可以尝试生成一个默认 key (例如类名+方法名)
            baseKey = signature.getDeclaringTypeName() + ":" + method.getName();
        }
        // todo 可以根据需要添加更多动态部分，例如用户 ID
        // // 获取当前用户 ID 的逻辑
        String finalKey = "rate_limit:" + baseKey;

        // 获取rate-limit注解中的参数
        int maxRequests = rateLimit.maxRequests();
        long windowSize = rateLimit.windowSize();
        TimeUnit timeUnit = rateLimit.timeUnit();
        String message = rateLimit.message();
        // 将窗口大小转换为毫秒
        long windowMillis = timeUnit.toMillis(windowSize);

        // 准备lua需要的参数
        List<String> keys = Collections.singletonList(finalKey);
        String maxRequestsStr = String.valueOf(maxRequests);
        String windowMillisStr = String.valueOf(windowMillis);
        String currentTimeMillisStr = String.valueOf(System.currentTimeMillis());

        // 执行 Lua 脚本
        // 需要注意lua脚本中获取参数的位置
        Long result;
        try {
            result = stringRedisTemplate.execute(redisScript, keys, maxRequestsStr, windowMillisStr, currentTimeMillisStr);
            log.debug("Rate limit check for key '{}': result = {}", finalKey, result);
        } catch (Exception e) {
            // redis异常出来，如果此时在 redis中执行出现问题，我们可以选择临时放行，或者直接拒绝
            log.error("Error executing rate limit script for key '{}'", finalKey, e);
            // return joinPoint.proceed(); // 临时放行
            return createRateLimitResponse(message); // 拒绝
        }

        if (result == null || result == 1) {
            // 允许访问
            log.info("Request allowed for key '{}'", finalKey);
            return joinPoint.proceed(); // 执行原方法
        } else {
            // 拒绝访问
            log.warn("Request denied for key '{}'. Limit exceeded.", finalKey);
            // 返回自定义的拒绝响应
            return createRateLimitResponse(message);
        }
    }
    // 辅助方法，用于创建限流后的响应对象
    private ResponseVo createRateLimitResponse(String message) {
        ResponseVo responseVo = new ResponseVo();
        // 可以定义一个特定的错误码表示限流
        responseVo.setCode(429); // 429 Too Many Requests
        responseVo.setMsg(message);
        return responseVo;
    }

}
