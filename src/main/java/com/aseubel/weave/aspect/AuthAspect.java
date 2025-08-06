package com.aseubel.weave.aspect;

import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.common.annotation.constraint.RequirePermission;
import com.aseubel.weave.common.exception.AuthenticationException;
import com.aseubel.weave.common.util.JwtUtil;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.entity.user.Role;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.redis.IRedisService;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aseubel.weave.common.Constant.HEADER_AUTHORIZATION;
import static com.aseubel.weave.common.Constant.TOKEN_PREFIX;

/**
 * 认证授权切面
 *
 * @author Aseubel
 * @date 2025/6/27
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthAspect {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final IRedisService redisService;

    /**
     * 登录校验切面
     */
    @Around("@annotation(requireLogin) || @within(requireLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint, RequireLogin requireLogin) throws Throwable {
//        if (requireLogin == null) {
//            requireLogin = joinPoint.getTarget().getClass().getAnnotation(RequireLogin.class);
//        }

//        if (requireLogin != null && requireLogin.value()) {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                throw new AuthenticationException("用户未登录或登录已过期");
            }
            // 将当前用户信息存储到ThreadLocal中，供后续使用
            UserContext.setCurrentUser(currentUser);
//        }

        try {
            return joinPoint.proceed();
        } finally {
            // 清理ThreadLocal
            UserContext.clear();
        }
    }

    /**
     * 权限校验切面
     */
    @Around("@annotation(requirePermission) || @within(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        if (requirePermission == null) {
            requirePermission = joinPoint.getTarget().getClass().getAnnotation(RequirePermission.class);
        }

        if (requirePermission != null) {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                throw new RuntimeException("用户未登录或登录已过期");
            }

            // 检查权限
            if (!hasPermission(currentUser, requirePermission)) {
                throw new RuntimeException("权限不足，无法访问该资源");
            }

            UserContext.setCurrentUser(currentUser);
        }

        try {
            return joinPoint.proceed();
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 获取当前登录用户
     */
    private User getCurrentUser() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }

        String token = extractToken(request);
        if (!StringUtils.hasText(token)) {
            return null;
        }

        // 验证token
        if (!jwtUtil.validateToken(token) || !jwtUtil.isAccessToken(token)) {
            return null;
        }

        // 检查token是否在Redis中（用于登出功能）
        Long userId = jwtUtil.getUserIdFromToken(token);
        String redisKey = KeyBuilder.userTokenKey(userId);
        String storedToken = redisService.getValue(redisKey);
        if (!token.equals(storedToken)) {
            return null;
        }

        // 获取用户信息
        return userService.findById(userId);
    }

    /**
     * 检查用户是否具有所需角色
     */
    private boolean hasPermission(User user, RequirePermission requirePermission) {
        Set<String> userRoles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        String[] requiredRoles = requirePermission.roles();

        // 如果没有指定角色要求，则允许访问
        if (requiredRoles.length == 0) {
            return true;
        }

        if (requirePermission.logical() == RequirePermission.LogicalOperator.AND) {
            // AND逻辑：需要同时满足所有角色
            return Arrays.stream(requiredRoles).allMatch(userRoles::contains);
        } else {
            // OR逻辑：满足任一角色即可
            return Arrays.stream(requiredRoles).anyMatch(userRoles::contains);
        }
    }

    /**
     * 从请求中提取token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * 获取当前请求
     */
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

}