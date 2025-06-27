package com.aseubel.weave.common.annotation;

import com.aseubel.weave.common.id.CustomSnowflakeIdGenerator;
import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:24
 */
@IdGeneratorType( CustomSnowflakeIdGenerator.class)
@Retention( RetentionPolicy.RUNTIME)
@Target({ FIELD, METHOD})
public @interface SnowflakeId {
}
