package com.aseubel.weave.common.annotation.cache;

import org.springframework.aot.hint.annotation.Reflective;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Aseubel
 * @date 2025/8/7 下午8:05
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Reflective
public @interface QueryCache {
    @AliasFor("cacheNames")
    String[] value() default {};

    @AliasFor("value")
    String[] cacheNames() default {};

    /**
     * 缓存的键，支持Spring Expression Language (SpEL)。
     * 例如： "#userId" 或 "#user.id"
     */
    String key() default "";

    SpelResolver spelResolver() default @SpelResolver(expression = "");
}
