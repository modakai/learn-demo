package com.sakura.demoratelimit.rate.token;

import com.sakura.demoratelimit.modal.ResponseVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 令牌桶算法
 */
@Slf4j
public class TokenBucketLimiter implements HandlerInterceptor {

    // 最后一次令牌发放的时间
    private static  long lastTime = System.currentTimeMillis();

    // 桶的容量
    private static final int capacity = 2;

    // 令牌生成的速度
    private static final int rate = 2;

    // 桶
    private static final AtomicInteger tokens = new AtomicInteger(0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long now = System.currentTimeMillis();
        long gap = now - lastTime; // 时间间隔

        // 计算时间段内令牌的数量
        int generatedTokens = (int) (gap * rate / 1000);
        if (generatedTokens > 0) { // 仅在有新增令牌时更新时间
            lastTime = now;
        }
        // 更新令牌数，不超过容量
        int currentTokens = tokens.updateAndGet(old ->
                Math.min(old + generatedTokens, capacity)
        );

        log.debug("当前令牌数: {}, 申请数: {}", currentTokens, 1);

        // 1 表示每次请求消耗的令牌数量
        if (currentTokens < 1) {
            // 拒绝请求
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = ResponseVo.responseJson();
            response.getWriter().write(json);
            log.warn("请求被限制：{}", request.getRequestURI());
            return false;
        } else {
            tokens.getAndAdd(-1);
            lastTime = now;
            log.info("请求放行：{}", request.getRequestURI());
            return true;
        }
    }
}
