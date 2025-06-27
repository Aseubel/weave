package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:06
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
