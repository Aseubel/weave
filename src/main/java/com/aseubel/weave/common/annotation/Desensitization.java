package com.aseubel.weave.common.annotation;

import com.aseubel.weave.common.desensitize.DesensitizationSerializer;
import com.aseubel.weave.common.desensitize.DesensitizationTypeEnum;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aseubel
 * @date 2025/8/1 下午9:36
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizationSerializer.class)
public @interface Desensitization {
    /**
     * 脱敏数据类型，在 CUSTOM 时，startInclude 和 endExclude生效
     */
    DesensitizationTypeEnum type() default DesensitizationTypeEnum.CUSTOM;

    /**
     * 脱敏数据开始位置（包含），在 CUSTOM 时生效
     */
    int startInclude() default 0;

    /**
     * 脱敏数据结束位置（不包含），在 CUSTOM 时生效
     */
    int endExclude() default 0;
}
