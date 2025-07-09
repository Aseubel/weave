package com.aseubel.weave.common.annotation;

import java.lang.annotation.*;

/**
 * @author Aseubel
 * @date 2025/7/5 下午4:03
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD})
public @interface TypeConvertor {

    String toTypeFullName() default "java.lang.String";

}
