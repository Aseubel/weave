package com.aseubel.weave.pojo.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

/**
 * @author Aseubel
 * @date 2025/6/27 下午6:37
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Column(unique = true, length = 50)
    private String username; // 自定义账号

    @Column(length = 100)
    private String password; // 存储加密后的密码

    @Column(nullable = false, length = 50)
    private String nickname; // 昵称

    @Column
    private String avatarUrl; // 头像链接

    @Column(unique = true, length = 20)
    private String mobile; // 手机号

    @Column(length = 100)
    private String wechatOpenId; // 微信OpenID

    @Column(length = 100)
    private String qqOpenId; // QQ OpenID

    @Column(length = 50)
    private String realName; // 真实姓名 (用于实名认证)

    @Column(length = 20)
    private String idCardNumber; // 身份证号 (加密存储)

    @Builder.Default
    private Long points = 0L; // 用户总积分

    @ManyToMany(fetch = FetchType.EAGER) // 用户角色通常需要立即加载
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // 兴趣标签，多对多
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_interest_tag",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    private Set<InterestTag> interestTags;
}
