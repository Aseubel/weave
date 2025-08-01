package com.aseubel.weave.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.aseubel.weave.common.exception.BusinessException;
import com.aseubel.weave.common.util.JwtUtil;
import com.aseubel.weave.pojo.dto.auth.*;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.dto.common.ThirdPartyLoginRequest;
import com.aseubel.weave.pojo.dto.user.UserInfoResponse;
import com.aseubel.weave.pojo.dto.user.UserUpdateRequest;
import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.user.CheckInStats;
import com.aseubel.weave.pojo.entity.user.InterestTag;
import com.aseubel.weave.pojo.entity.user.Role;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.CheckInRepository;
import com.aseubel.weave.repository.CheckInStatsRepository;
import com.aseubel.weave.repository.InterestTagRepository;
import com.aseubel.weave.repository.UserRepository;
import com.aseubel.weave.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.aseubel.weave.common.Constant.SMS_CODE_EXPIRE_MINUTES;
import static com.aseubel.weave.pojo.dto.common.ThirdPartyLoginRequest.LoginType.QQ;
import static com.aseubel.weave.pojo.dto.common.ThirdPartyLoginRequest.LoginType.WECHAT;

/**
 * 用户服务实现类
 * 
 * @author Aseubel
 * @date 2025/6/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CheckInRepository checkInRepository;
    private final CheckInStatsRepository checkInStatsRepository;
    private final InterestTagRepository interestTagRepository;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final WebClient.Builder webClientBuilder;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @Value("${third-party.wechat.app-id}")
    private String wechatAppId;

    @Value("${third-party.wechat.app-secret}")
    private String wechatAppSecret;

    @Value("${third-party.qq.app-id}")
    private String qqAppId;

    @Value("${third-party.qq.app-key}")
    private String qqAppKey;

    @Value("${app.password}")
    private String defaultPassword;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 查找用户
        User user = userRepository.findByUsernameOrMobile(request.getLoginId())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 验证密码
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        return generateTokenResponse(user);
    }

    @Override
    @Transactional
    public LoginResponse mobileLogin(MobileLoginRequest request) {
        // 验证短信验证码
        if (!verifySmsCode(request.getMobile(), request.getSmsCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 查找或创建用户
        User user = userRepository.findByMobile(request.getMobile())
                .orElseGet(() -> {
                    // 如果用户不存在，自动注册
                    User newUser = User.builder()
                            .mobile(request.getMobile())
                            .password(BCrypt.hashpw(defaultPassword, BCrypt.gensalt()))
                            .nickname("用户" + request.getMobile().substring(7))
                            .username("mobile_" + request.getMobile())
                            .build();
                    return userRepository.save(newUser);
                });

        return generateTokenResponse(user);
    }

    @Override
    @Transactional
    public LoginResponse thirdPartyLogin(ThirdPartyLoginRequest request) {
        String openId;
        String nickname;
        String avatarUrl;

        if (WECHAT.equals(request.getType())) {
            // 微信登录逻辑
            WechatUserInfo wechatUserInfo = getWechatUserInfo(request.getCode());
            openId = wechatUserInfo.getOpenid();
            nickname = wechatUserInfo.getNickname();
            avatarUrl = wechatUserInfo.getHeadimgurl();

            // 查找或创建用户
            User user = userRepository.findByWechatOpenId(openId)
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .wechatOpenId(openId)
                                .nickname(nickname)
                                .avatar(Image.builder().imageUrl(avatarUrl).build())
                                .username("wechat_" + openId.substring(0, 8))
                                .build();
                        return userRepository.save(newUser);
                    });

            return generateTokenResponse(user);
        } else if (QQ.equals(request.getType())) {
            // QQ登录逻辑
            QQUserInfo qqUserInfo = getQQUserInfo(request.getCode());
            openId = qqUserInfo.getOpenid();
            nickname = qqUserInfo.getNickname();
            avatarUrl = qqUserInfo.getFigureurl_qq_1();

            // 查找或创建用户
            User user = userRepository.findByQqOpenId(openId)
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .qqOpenId(openId)
                                .nickname(nickname)
                                .avatar(Image.builder().imageUrl(avatarUrl).build())
                                .username("qq_" + openId.substring(0, 8))
                                .build();
                        return userRepository.save(newUser);
                    });

            return generateTokenResponse(user);
        } else {
            throw new RuntimeException("不支持的第三方平台");
        }
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // 验证密码确认
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 检查用户名是否存在
        if (existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号是否存在（如果提供了手机号）
        if (StringUtils.hasText(request.getMobile())) {
            if (existsByMobile(request.getMobile())) {
                throw new RuntimeException("手机号已被注册");
            }
            // 验证短信验证码
            if (!verifySmsCode(request.getMobile(), request.getSmsCode())) {
                throw new RuntimeException("验证码错误或已过期");
            }
        }

        // 创建用户
        User user = User.builder()
                .username(request.getUsername())
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .nickname(request.getNickname())
                .mobile(request.getMobile())
                .build();

        user = userRepository.save(user);
        return generateTokenResponse(user);
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // 验证刷新令牌
        if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            throw new RuntimeException("刷新令牌无效或已过期");
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return generateTokenResponse(user);
    }

    @Override
    public void logout(Long userId) {
        // 从Redis中删除token
        String redisKey = KeyBuilder.userTokenKey(userId);
        redisTemplate.delete(redisKey);
    }

    @Override
    public String sendSmsCode(String mobile) {
        // 生成6位数字验证码
        String code = RandomUtil.randomNumbers(6);

        // 存储到Redis，5分钟过期
        String redisKey = KeyBuilder.smsCodeKey(mobile);
        redisTemplate.opsForValue().set(redisKey, code, SMS_CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 这里应该调用短信服务发送验证码
        // 为了演示，我们只是记录日志
        log.info("发送短信验证码到 {}: {}", mobile, code);
        return code;

        // TODO 实际项目中应该调用阿里云、腾讯云等短信服务
        // sendSmsViaSmsProvider(mobile, code, type);
    }

    @Override
    public boolean verifySmsCode(String mobile, String code) {
        String redisKey = KeyBuilder.smsCodeKey(mobile);
        String storedCode = (String) redisTemplate.opsForValue().get(redisKey);

        if (storedCode != null && storedCode.equals(code)) {
            // 验证成功后删除验证码
            redisTemplate.delete(redisKey);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByMobile(String mobile) {
        return userRepository.existsByMobile(mobile);
    }

    /**
     * 生成token响应
     */
    private LoginResponse generateTokenResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        // 将访问令牌存储到Redis
        String redisKey = KeyBuilder.userTokenKey(user.getId());
        redisTemplate.opsForValue().set(redisKey, accessToken, accessTokenExpiration, TimeUnit.MILLISECONDS);

        return LoginResponse.fromUser(user, accessToken, refreshToken,
                System.currentTimeMillis() + accessTokenExpiration,
                System.currentTimeMillis() + refreshTokenExpiration);
    }

    /**
     * 获取微信用户信息
     */
    private WechatUserInfo getWechatUserInfo(String code) {
        // 1. 通过code获取access_token
        String tokenUrl = String.format(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                wechatAppId, wechatAppSecret, code);

        // 这里应该实际调用微信API
        // 为了演示，返回模拟数据
        WechatUserInfo userInfo = new WechatUserInfo();
        userInfo.setOpenid("mock_wechat_openid_" + code.substring(0, 8));
        userInfo.setNickname("微信用户");
        userInfo.setHeadimgurl("https://example.com/avatar.jpg");

        return userInfo;
    }

    /**
     * 获取QQ用户信息
     */
    private QQUserInfo getQQUserInfo(String code) {
        // 1. 通过code获取access_token
        // 2. 通过access_token获取openid
        // 3. 通过access_token和openid获取用户信息

        // 这里应该实际调用QQ API
        // 为了演示，返回模拟数据
        QQUserInfo userInfo = new QQUserInfo();
        userInfo.setOpenid("mock_qq_openid_" + code.substring(0, 8));
        userInfo.setNickname("QQ用户");
        userInfo.setFigureurl_qq_1("https://example.com/avatar.jpg");

        return userInfo;
    }

    // 微信用户信息DTO
    private static class WechatUserInfo {
        private String openid;
        private String nickname;
        private String headimgurl;

        // getters and setters
        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }
    }

    // QQ用户信息DTO
    private static class QQUserInfo {
        private String openid;
        private String nickname;
        private String figureurl_qq_1;

        // getters and setters
        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getFigureurl_qq_1() {
            return figureurl_qq_1;
        }

        public void setFigureurl_qq_1(String figureurl_qq_1) {
            this.figureurl_qq_1 = figureurl_qq_1;
        }
    }

    @Override
    public UserInfoResponse getUserInfo(User user) {
        // 获取签到统计信息
        CheckInStats checkInStats = checkInStatsRepository.findByUser(user).orElse(null);
        UserInfoResponse.CheckInInfo checkInInfo = null;

        if (checkInStats != null) {
            checkInInfo = UserInfoResponse.CheckInInfo.builder()
                    .totalCheckDays(checkInStats.getTotalCheckDays())
                    .currentConsecutiveDays(checkInStats.getCurrentConsecutiveDays())
                    .maxConsecutiveDays(checkInStats.getMaxConsecutiveDays())
                    .lastCheckDate(checkInStats.getLastCheckDate())
                    .todayChecked(checkInRepository.existsByUserAndCheckDate(user, LocalDate.now()))
                    .availableMakeUp(checkInStats.getAvailableMakeUp())
                    .build();
        }

        // 构建角色信息
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        // 构建兴趣标签信息
        Set<UserInfoResponse.InterestTagInfo> interestTagInfos = user.getInterestTags().stream()
                .map(tag -> UserInfoResponse.InterestTagInfo.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .color(tag.getColor())
                        .build())
                .collect(Collectors.toSet());

        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .bio(user.getBio())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .city(user.getCity())
                .profession(user.getProfession())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .avatar(user.getAvatar())
                .points(user.getPoints())
                .level(user.getLevel())
                .createTime(user.getCreatedAt())
                .lastLoginTime(user.getLastLoginTime())
                .isActive(user.getIsActive())
                .roles(roleNames)
                .interestTags(interestTagInfos)
                .checkInInfo(checkInInfo)
                .build();
    }

    @Override
    @Transactional
    public void updateUserInfo(User user, UserUpdateRequest request) {
        // 更新基本信息
        if (StringUtils.hasText(request.getNickname())) {
            user.setNickname(request.getNickname());
        }
        if (StringUtils.hasText(request.getBio())) {
            user.setBio(request.getBio());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }
        if (StringUtils.hasText(request.getGender())) {
            user.setGender(request.getGender());
        }
        if (StringUtils.hasText(request.getCity())) {
            user.setCity(request.getCity());
        }
        if (StringUtils.hasText(request.getProfession())) {
            user.setProfession(request.getProfession());
        }
        if (StringUtils.hasText(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getMobile())) {
            // 检查手机号是否已被其他用户使用
            if (!request.getMobile().equals(user.getMobile()) &&
                    userRepository.existsByMobile(request.getMobile())) {
                throw new BusinessException("手机号已被使用");
            }
            user.setMobile(request.getMobile());
        }
        if (ObjectUtil.isNotEmpty(request.getAvatarId())) {
            user.setAvatar(Image.builder().id(request.getAvatarId()).build());
        }

        // 更新兴趣标签
        if (request.getInterestTagIds() != null) {
            Set<InterestTag> interestTags = new HashSet<>(interestTagRepository.findAllById(request.getInterestTagIds()));
            user.setInterestTags(interestTags);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        // 验证确认密码
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 验证原密码
        if (!BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 检查新密码是否与原密码相同
        if (BCrypt.checkpw(request.getNewPassword(), user.getPassword())) {
            throw new BusinessException("新密码不能与原密码相同");
        }

        // 更新密码
        user.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        userRepository.save(user);

        // 清除用户的所有token，强制重新登录
        String tokenKey = KeyBuilder.userTokenKey(user.getId());
        redisTemplate.delete(tokenKey);

        log.info("用户 {} 修改密码成功", user.getUsername());
    }

    @Override
    public PageResponse<UserInfoResponse> getUserList(int page, int size, String keyword) {
        Sort.TypedSort<User> sortType = Sort.sort(User.class);
        Pageable pageable = PageRequest.of(page - 1, size, sortType.by(User::getCreatedAt).descending());

        Page<User> userPage;
        if (StringUtils.hasText(keyword)) {
            userPage = userRepository.findByUsernameContainingOrNicknameContainingOrMobileContaining(
                    keyword, keyword, keyword, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        List<UserInfoResponse> userInfoList = userPage.getContent().stream()
                .map(this::getUserInfo)
                .collect(Collectors.toList());

        return PageResponse.of(userInfoList, page, size, userPage.getTotalElements());
    }

    @Override
    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        user.setIsActive(!user.getIsActive());
        userRepository.save(user);

        // 如果禁用用户，清除其token
        if (!user.getIsActive()) {
            String tokenKey = KeyBuilder.userTokenKey(userId);
            redisTemplate.delete(tokenKey);
        }

        log.info("用户 {} 状态已更新为: {}", user.getUsername(), user.getIsActive() ? "启用" : "禁用");
    }
}