package com.sakura.demoratelimit.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流资源的唯一标识 Key
     */
    String key() default "";

    /**
     * 时间窗口内允许的最大请求次数
     */
    int maxRequests();

    /**
     * 时间窗口大小
     */
    long windowSize();

    /**
     * 时间窗口单位 (默认为毫秒)
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 获取不到令牌时的提示信息
     */
    String message() default "操作过于频繁，请稍后再试";
}
