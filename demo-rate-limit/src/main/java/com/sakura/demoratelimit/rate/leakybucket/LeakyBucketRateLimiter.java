package com.sakura.demoratelimit.rate.leakybucket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakura.demoratelimit.modal.ResponseVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 漏桶限流器
 */
@Slf4j
public class LeakyBucketRateLimiter implements HandlerInterceptor {

    // 计算的起始时间
    private static long lastOutTime = System.currentTimeMillis();
    // 流出速率 每秒 2 次
    private static int leakRate = 2;
    // 桶的容量
    private static int capacity = 2;

    //剩余的水量
    private static final AtomicInteger water = new AtomicInteger(0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 如果是空桶，就当前时间作为漏出的时间
        if (water.get() == 0) {
            lastOutTime = System.currentTimeMillis();
            water.set(1);
            log.info("当前水量：{}", water.get());
            // 表示没有被限制
            return true;
        }
        log.info("当前水量：{}", water.get());
        // 执行漏水
        int waterLeaked = ((int) ((System.currentTimeMillis() - lastOutTime) / 1000)) * leakRate;
        // 计算剩余水量
        int waterLeft = water.get() - waterLeaked;
        water.set(Math.max(0, waterLeft));
        // 重新更新leakTimeStamp
        lastOutTime = System.currentTimeMillis();
        // 尝试加水,并且水还未满 ，放行
        if (water.get() < capacity) {
            water.addAndGet(1);
        } else {
            // 拒绝请求
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = ResponseVo.responseJson();
            response.getWriter().write(json);
            log.warn("请求被限制：{}", request.getRequestURI());
            return false;
        }
        log.info("请求放行：{}", request.getRequestURI());
        return true;
    }


}
