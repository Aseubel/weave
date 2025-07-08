package com.aseubel.weave.common.exception;

/**
 * 认证异常
 * @author Aseubel
 * @date 2025/1/27
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}