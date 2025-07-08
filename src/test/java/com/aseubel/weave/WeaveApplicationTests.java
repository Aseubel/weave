package com.aseubel.weave;

import cn.hutool.crypto.digest.BCrypt;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.repository.UserRepository;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootTest
class WeaveApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

//    @Test
    void testJpa() {
        User user = new User();
        user.setUsername("aseubel");
        user.setPassword("noway");
        user.setNickname("akun");
        user.setAvatar("avatarUrl");
        Long id = userRepository.save(user).getId();
        System.out.println(id);
        userRepository.deleteById(id);
    }

    @Test
    public void generateSecret() {
        // 生成一个符合HS512要求的密钥
        SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);

        // 获取密钥的字节数组
        byte[] secretKeyBytes = secretKey.getEncoded();

        // 使用Base64编码将字节数组转换为字符串
        String secretKeyString = Base64.getEncoder().encodeToString(secretKeyBytes);

        // 输出密钥字符串
        System.out.println("生成的HS512密钥的Base64编码字符串为: " + secretKeyString);
    }

    @Test
    public void setUserPassword() {
        String password = "Abcd1234";
        User user = new User();
        user.setId(1939884796922695680L);
        user = userRepository.findById(user.getId()).get();
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        userRepository.save(user);
    }

}
