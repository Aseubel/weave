package com.aseubel.weave.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @author Aseubel
 * @date 2025/7/5 下午4:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface FieldDesc {
    String name() default "";
}
