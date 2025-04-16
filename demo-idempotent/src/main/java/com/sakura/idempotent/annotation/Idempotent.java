package com.sakura.idempotent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口幂等注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 过期时间（秒）
     */
    int expireSeconds() default 60;

    /**
     * 幂等键的来源，默认从请求参数获取
     */
    KeySource keySource() default KeySource.PARAMETER;

    /**
     * 幂等键的参数名，如果source为PARAMETER时使用
     */
    String keyName() default "";

    /**
     * 幂等键前缀
     */
    String prefix() default "idempotent:";

    enum KeySource {
        PARAMETER, // 从请求参数获取
        HEADER,    // 从请求头获取
        TOKEN      // 使用全局token
    }
}
