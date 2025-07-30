package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Aseubel
 * @date 2025/7/30 下午12:26
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
}
