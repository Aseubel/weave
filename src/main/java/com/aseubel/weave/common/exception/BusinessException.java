package com.aseubel.weave.common.exception;

import lombok.Getter;

/**
 * 业务异常
 * @author Aseubel
 * @date 2025/1/27
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final int code;
    
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public BusinessException(String message) {
        this(400, message);
    }
    
    public BusinessException(String message, Throwable cause) {
        this(400, message, cause);
    }
}