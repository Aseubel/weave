package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.competition.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Aseubel
 * @date 2025/7/29 上午11:39
 */
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Participant findByUserId(Long userId);
}
