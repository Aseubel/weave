package com.aseubel.weave.task.competition;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.PostRepository;
import com.aseubel.weave.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

import static com.aseubel.weave.redis.LuaScript.HASH_GET_REMOVE_SCRIPT;

/**
 * @author Aseubel
 * @date 2025/7/29 上午11:55
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SubmissionSyncVote {

    private final SubmissionRepository repository;
    private final StringRedisTemplate redisTemplate;

    // 每分钟同步一次点赞数
    @Scheduled(fixedRate = 60000)
    public void syncSubmissionVote() {
        String key = KeyBuilder.submissionVoteRecentKey();
        Map<Long, Long> submissionSet = (Map<Long, Long>) redisTemplate.execute(HASH_GET_REMOVE_SCRIPT, Collections.singletonList(key));
        if (CollectionUtil.isNotEmpty(submissionSet)) {
            for (Map.Entry<Long, Long> entry : submissionSet.entrySet()) {
                Long postId = entry.getKey();
                Long likeCount = entry.getValue();
                repository.updateVoteCount(postId, likeCount);
            }
            log.info("Sync post like count success, count: {}", submissionSet.size());
        }
    }

}
