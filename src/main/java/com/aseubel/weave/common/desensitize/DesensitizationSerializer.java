package com.aseubel.weave.common.desensitize;

import cn.hutool.core.util.ObjectUtil;
import com.aseubel.weave.common.annotation.Desensitization;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @author Aseubel
 * @description 自定义序列化器，用于对敏感信息进行脱敏
 * @date 2025/8/1 下午9:37
 */
@AllArgsConstructor
@NoArgsConstructor
public class DesensitizationSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizationTypeEnum type;
    private Integer startInclude;
    private Integer endExclude;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (type == null) {
            jsonGenerator.writeString(s);
        } else {
            jsonGenerator.writeString(type.desensitize(s, startInclude, endExclude));
            System.out.println("序列化器被调用 " + s);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            // 判断数据类型是否为String类型
            if (ObjectUtil.equals(beanProperty.getType().getRawClass(), String.class)) {
                // 获取注解信息
                Desensitization desensitization = beanProperty.getAnnotation(Desensitization.class);
                // 如果字段上没有注解，从上下文中获取注解信息
                if (desensitization == null) {
                    desensitization = beanProperty.getContextAnnotation(Desensitization.class);
                }
                // 如果注解信息存在，则创建DesensitizationSerializer
                if (desensitization != null) {
                    return new DesensitizationSerializer(desensitization.type(), desensitization.startInclude(), desensitization.endExclude());
                }
            }
            // 如果数据类型不是String类型，则使用默认的序列化器
            // return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
            // 如果字段不是String类型，或者是一个没有@Desensitization注解的String类型，
            // 则返回 'this'，即返回在JacksonConfig中注册的那个默认实例。
            return this;
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
