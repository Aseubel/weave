package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.user.UserPointRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Aseubel
 * @date 2025/8/1 下午11:24
 */
public interface UserPointRecordRepository extends JpaRepository<UserPointRecord, Long> {
}
