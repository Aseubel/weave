package com.aseubel.weave.common;

import com.aseubel.weave.common.constants.BaseEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

/**
 * @author Aseubel
 * @date 2025/7/28 下午4:56
 */
@Converter
public class GenericEnumConverter<T extends Enum<T> & BaseEnum<T>>
        implements AttributeConverter<T, Integer> {

    private final Class<T> enumType;

    public GenericEnumConverter(Class<T> enumType) {
        this.enumType = enumType;
    }

    // 无参构造器需要配合@AutoApply使用
    public GenericEnumConverter() {
        this.enumType = resolveEnumType();
    }

    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public T convertToEntityAttribute(Integer code) {
        return code != null ?
                Optional.ofNullable(BaseEnum.parseByCode(enumType, code)).orElseThrow(() ->
                        new IllegalArgumentException("无效的枚举编码: " + code))
                : null;
    }

    /**
     * 通过反射获取泛型实际类型
     */
    @SuppressWarnings("unchecked")
    private Class<T> resolveEnumType() {
        ParameterizedType type = (ParameterizedType) getClass()
                .getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }
}

