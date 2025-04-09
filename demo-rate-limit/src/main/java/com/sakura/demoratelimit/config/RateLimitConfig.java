package com.sakura.demoratelimit.config;

import com.sakura.demoratelimit.rate.leakybucket.LeakyBucketRateLimiter;
import com.sakura.demoratelimit.rate.token.TokenBucketLimiter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RateLimitConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SlidingWindowRateLimiter()).addPathPatterns("/api/index");

//        registry.addInterceptor(new LeakyBucketRateLimiter()).addPathPatterns("/api/index");

        registry.addInterceptor(new TokenBucketLimiter()).addPathPatterns("/api/index");
    }
}