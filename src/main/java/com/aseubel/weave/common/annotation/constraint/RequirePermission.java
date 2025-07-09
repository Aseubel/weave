package com.aseubel.weave.common.annotation.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要角色注解
 * @author Aseubel
 * @date 2025/6/27
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    /**
     * 需要的角色名称
     */
    String[] roles() default {};
    
    /**
     * 逻辑关系：AND(需要同时满足所有角色) 或 OR(满足任一角色即可)
     */
    LogicalOperator logical() default LogicalOperator.OR;
    
    enum LogicalOperator {
        AND, OR
    }
}