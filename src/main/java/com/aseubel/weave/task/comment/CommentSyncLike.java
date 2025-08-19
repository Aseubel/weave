package com.aseubel.weave.task.comment;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.weave.pojo.entity.comment.Comment;
import com.aseubel.weave.redis.IRedisService;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.aseubel.weave.redis.LuaScript.HASH_GET_REMOVE_SCRIPT;

/**
 * @author Aseubel
 * @date 2025/7/13 下午12:08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommentSyncLike {

    private final CommentRepository commentRepository;
    private final IRedisService redisService;

    // 每隔 1 秒同步一次点赞数
    @Transactional
    @Scheduled(fixedRate = 1000)
    public void syncCommentLike() {
        String key = KeyBuilder.commentLikeCountKey();
        if (!redisService.isExists(key)) {
            return;
        }
        List<Object> commentList = redisService.execute(
                HASH_GET_REMOVE_SCRIPT, RScript.ReturnType.MAPVALUE, Collections.singletonList(key), 0);
        if (CollectionUtil.isNotEmpty(commentList)) {
            // 从 List 手动构建 Map
            Map<String, String> commentMap = new HashMap<>();
            // HGETALL 返回的 List 是 [key1, value1, key2, value2, ...] 的形式，所以步长为 2
            for (int i = 0; i < commentList.size(); i += 2) {
                commentMap.put(String.valueOf(commentList.get(i)), String.valueOf(commentList.get(i + 1)));
            }
            Set<Long> commentIdsToUpdate = commentMap.keySet().stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
            // 拿到所有需要更新的 Comment
            List<Comment> commentsToUpdate = commentRepository.findAllById(commentIdsToUpdate);
            for (Comment comment : commentsToUpdate) {
                Integer newLikeCount = Integer.parseInt(commentMap.get(String.valueOf(comment.getId()))) + comment.getLikeCount();
                comment.setLikeCount(newLikeCount);
            }
            if (CollectionUtil.isNotEmpty(commentsToUpdate)) {
                commentRepository.saveAll(commentsToUpdate);
                log.info("Sync comment like count success, count: {}", commentMap.size());
            }
        }
    }

}
