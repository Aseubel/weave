//package com.aseubel.weave.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Spring Security配置类
// * @author Aseubel
// * @date 2025/1/27
// */
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            // 禁用CSRF（因为我们使用JWT）
//            .csrf(AbstractHttpConfigurer::disable)
//
//            // 配置CORS
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//
//            // 配置会话管理为无状态
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//            // 配置授权规则
//            .authorizeHttpRequests(authz -> authz
//                // 公开的认证接口
//                .requestMatchers("/api/auth/**").permitAll()
//                // 公开的测试接口
//                .requestMatchers("/api/test/public").permitAll()
//                .requestMatchers("/api/test/redis").permitAll()
//                // 健康检查接口
//                .requestMatchers("/actuator/**").permitAll()
//                // 静态资源
//                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
//                // 其他所有请求都需要认证（通过AOP处理）
//                .anyRequest().permitAll()
//            )
//
//            // 禁用默认的登录页面
//            .formLogin(AbstractHttpConfigurer::disable)
//
//            // 禁用HTTP Basic认证
//            .httpBasic(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }
//
//    /**
//     * 密码编码器
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * CORS配置
//     */
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // 允许的源
//        configuration.setAllowedOriginPatterns(List.of("*"));
//
//        // 允许的HTTP方法
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//
//        // 允许的请求头
//        configuration.setAllowedHeaders(List.of("*"));
//
//        // 允许发送Cookie
//        configuration.setAllowCredentials(true);
//
//        // 预检请求的缓存时间
//        configuration.setMaxAge(3600L);
//
//        // 暴露的响应头
//        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }
//}