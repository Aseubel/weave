package com.aseubel.weave.task.competition;

import com.aseubel.weave.redis.KeyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 重置用户拥有的票数
 * @author Aseubel
 * @date 2025/7/29 下午12:57
 */
@Service
@Slf4j
public class ResetUserVotesNumber {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 每天0点重置用户票数
     * 即清除记录的redis中的票数
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetUserVotesNumber() {
        redisTemplate.delete(KeyBuilder.competitionVoteStatusKey() + "*");
    }
}
