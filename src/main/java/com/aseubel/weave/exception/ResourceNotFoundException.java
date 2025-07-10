package com.aseubel.weave.exception;

import lombok.Getter;

/**
 * 资源未找到异常
 * @author Aseubel
 * @date 2025/7/8
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {
    
    private final int code;
    
    public ResourceNotFoundException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public ResourceNotFoundException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public ResourceNotFoundException(String message) {
        this(404, message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        this(404, message, cause);
    }
}