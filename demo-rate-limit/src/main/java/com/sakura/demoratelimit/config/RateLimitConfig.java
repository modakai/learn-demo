package com.sakura.demoratelimit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RateLimitConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SlidingWindowRateLimiter()).addPathPatterns("/api/index");

//        registry.addInterceptor(new LeakyBucketRateLimiter()).addPathPatterns("/api/index");

//        registry.addInterceptor(new TokenBucketLimiter()).addPathPatterns("/api/index");
    }
}