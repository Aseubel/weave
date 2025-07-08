package com.aseubel.weave.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应格式
 * @author Aseubel
 * @date 2025/6/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * 响应状态码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 请求是否成功
     */
    private boolean success;
    
    /**
     * 时间戳
     */
    private long timestamp;
    
    /**
     * 成功响应
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("操作成功");
        response.setData(data);
        response.setSuccess(true);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
    
    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }
    
    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        response.setSuccess(true);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
    
    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(null);
        response.setSuccess(false);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
    
    /**
     * 失败响应（默认500错误码）
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }
    
    /**
     * 参数错误响应
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(400, message);
    }
    
    /**
     * 未授权响应
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message);
    }
    
    /**
     * 禁止访问响应
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return error(403, message);
    }
    
    /**
     * 资源不存在响应
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }
}