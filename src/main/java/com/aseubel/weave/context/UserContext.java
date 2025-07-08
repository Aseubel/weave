package com.aseubel.weave.context;

import com.aseubel.weave.pojo.entity.user.User;

/**
 * 用户上下文，用于在同一请求中传递用户信息
 * @author Aseubel
 * @date 2025/6/27
 */
public class UserContext {
    
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();
    
    /**
     * 设置当前用户
     */
    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }
    
    /**
     * 获取当前用户
     */
    public static User getCurrentUser() {
        return currentUser.get();
    }
    
    /**
     * 清除当前用户信息
     */
    public static void clear() {
        currentUser.remove();
    }
    
    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }
    
    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        User user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }
}