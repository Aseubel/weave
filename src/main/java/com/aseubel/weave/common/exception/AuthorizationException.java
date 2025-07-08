package com.aseubel.weave.common.exception;

/**
 * 授权异常
 * @author Aseubel
 * @date 2025/1/27
 */
public class AuthorizationException extends RuntimeException {
    
    public AuthorizationException(String message) {
        super(message);
    }
    
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}