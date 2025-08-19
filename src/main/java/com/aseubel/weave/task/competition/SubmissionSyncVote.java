package com.aseubel.weave.task.competition;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.weave.redis.IRedisService;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    private final IRedisService redisService;

    // 每隔 1 秒同步一次投票数
    @Scheduled(fixedRate = 1000)
    public void syncSubmissionVote() {
        String key = KeyBuilder.submissionVoteCountKey();
        if (!redisService.isExists(key)) {
            return;
        }
        List<Object> submissionList = redisService.execute(
                HASH_GET_REMOVE_SCRIPT, RScript.ReturnType.MAPVALUE, Collections.singletonList(key), 0);
        if (CollectionUtil.isNotEmpty(submissionList)) {
            // 从 List 手动构建 Map
            Map<String, String> submissionMap = new HashMap<>();
            // HGETALL 返回的 List 是 [key1, value1, key2, value2, ...] 的形式，所以步长为 2
            for (int i = 0; i < submissionList.size(); i += 2) {
                submissionMap.put(String.valueOf(submissionList.get(i)), String.valueOf(submissionList.get(i + 1)));
            }
            for (Map.Entry<String, String> entry : submissionMap.entrySet()) {
                Long postId = Long.parseLong(entry.getKey());
                Long likeCount = Long.parseLong(entry.getValue());
                repository.updateVoteCount(postId, likeCount);
            }
            log.info("Sync post like count success, count: {}", submissionMap.size());
        }
    }

}
