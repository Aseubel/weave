package com.aseubel.weave.common.annotation.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要登录注解
 * @author Aseubel
 * @date 2025/6/27
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireLogin {
    /**
     * 是否必须登录，默认true
     */
    boolean value() default true;
}