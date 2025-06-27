package com.aseubel.weave;

import com.aseubel.weave.pojo.entity.User;
import com.aseubel.weave.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WeaveApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testJpa() {
        User user = new User();
        user.setUsername("aseubel");
        user.setPassword("noway");
        user.setNickname("akun");
        user.setAvatarUrl("avatarUrl");
        System.out.println(userRepository.save(user).getId());
    }

}
