package com.aseubel.weave.exception;

import lombok.Getter;

/**
 * 未授权异常
 * @author Aseubel
 * @date 2025/1/15
 */
@Getter
public class UnauthorizedException extends RuntimeException {
    
    private final int code;
    
    public UnauthorizedException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public UnauthorizedException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public UnauthorizedException(String message) {
        this(403, message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        this(403, message, cause);
    }
}