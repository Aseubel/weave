package com.aseubel.weave.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * IP工具类
 * @author Aseubel
 * @date 2025/6/29
 */
@Slf4j
public class IpUtil {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * 获取客户端真实IP地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多个IP的情况，取第一个非unknown的有效IP
        if (ip != null && ip.contains(",")) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!UNKNOWN.equalsIgnoreCase(strIp.trim())) {
                    ip = strIp.trim();
                    break;
                }
            }
        }

        // 处理本地地址
        if (LOCALHOST_IPV6.equals(ip)) {
            ip = LOCALHOST_IPV4;
        }

        return ip;
    }

    /**
     * 获取设备信息
     */
    public static String getDeviceInfo(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) {
            return UNKNOWN;
        }

        // 简化设备信息提取
        StringBuilder deviceInfo = new StringBuilder();
        
        // 操作系统
        if (userAgent.contains("Windows")) {
            deviceInfo.append("Windows");
        } else if (userAgent.contains("Mac")) {
            deviceInfo.append("Mac");
        } else if (userAgent.contains("Linux")) {
            deviceInfo.append("Linux");
        } else if (userAgent.contains("Android")) {
            deviceInfo.append("Android");
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            deviceInfo.append("iOS");
        } else {
            deviceInfo.append("Unknown OS");
        }

        deviceInfo.append(" - ");

        // 浏览器
        if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) {
            deviceInfo.append("Chrome");
        } else if (userAgent.contains("Firefox")) {
            deviceInfo.append("Firefox");
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            deviceInfo.append("Safari");
        } else if (userAgent.contains("Edg")) {
            deviceInfo.append("Edge");
        } else if (userAgent.contains("Opera")) {
            deviceInfo.append("Opera");
        } else {
            deviceInfo.append("Unknown Browser");
        }

        return deviceInfo.toString();
    }

    /**
     * 判断是否为内网IP
     */
    public static boolean isInternalIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        if (LOCALHOST_IPV4.equals(ip) || LOCALHOST_IPV6.equals(ip)) {
            return true;
        }

        try {
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            int firstOctet = Integer.parseInt(parts[0]);
            int secondOctet = Integer.parseInt(parts[1]);

            // 10.0.0.0 - 10.255.255.255
            if (firstOctet == 10) {
                return true;
            }

            // 172.16.0.0 - 172.31.255.255
            if (firstOctet == 172 && secondOctet >= 16 && secondOctet <= 31) {
                return true;
            }

            // 192.168.0.0 - 192.168.255.255
            if (firstOctet == 192 && secondOctet == 168) {
                return true;
            }

        } catch (NumberFormatException e) {
            log.warn("Invalid IP format: {}", ip);
            return false;
        }

        return false;
    }
}