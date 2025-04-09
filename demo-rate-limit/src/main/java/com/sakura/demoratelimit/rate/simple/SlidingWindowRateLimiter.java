package com.sakura.demoratelimit.rate.simple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakura.demoratelimit.modal.ResponseVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 基于滑动窗口算法的限流器
 */
@Slf4j
public class SlidingWindowRateLimiter implements HandlerInterceptor {

    private final Map<String, Deque<Long>> requestLogs = new ConcurrentHashMap<>();
    private final int windowSizeMillis = 1000; // 1秒窗口
    private final int maxRequests = 5; // 最大请求数

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 请求路径
        String key = request.getRequestURI();
        // 当前请求时间
        long now = System.currentTimeMillis();

        // 清理过期请求
        Deque<Long> logs = requestLogs.computeIfAbsent(key, k -> new ConcurrentLinkedDeque<>());
        logs.removeIf(timestamp -> timestamp < now - windowSizeMillis);
        log.info("当前请求URI：{}", key);
        log.info("当前请求数：{}", logs.size());
        if (logs.size() >= maxRequests) {
            // 拒绝请求
//            ResponseVo.responseJson(response);
            log.warn("当前请求URI：{} 被拒绝", key);
            return false;
        } else {
            // 记录请求
            log.info("当前请求URI：{} 已放行", key);
            logs.addLast(now);
            return true;
        }

    }
}
