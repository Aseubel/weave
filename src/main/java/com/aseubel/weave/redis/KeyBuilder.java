package com.aseubel.weave.redis;

import static com.aseubel.weave.redis.RedisKey.*;

/**
 * @author Aseubel
 * @date 2025/6/28 下午9:22
 */
public class KeyBuilder {
    public static String userTokenKey(Long userId) {
        return APP + REDIS_TOKEN_KEY_PREFIX + userId;
    }

    public static String smsCodeKey(String mobile) {
        return APP + REDIS_SMS_CODE_PREFIX + mobile;
    }

    /**
     * set
     */
    public static String postLikeRecentKey() {
        return APP + POST_LIKE_RECENT_KEY;
    }

    /**
     * map 记录点赞数的增量
     */
    public static String postLikeCountKey() {
        return APP + POST_LIKE_COUNT_KEY;
    }

    /**
     * set 记录点赞的用户
     */
    public static String postLikeStatusKey(Long postId) {
        return APP + POST_LIKE_STATUS_KEY + postId;
    }

    /**
     * set
     */
    public static String commentLikeRecentKey() {
        return APP + COMMENT_LIKE_RECENT_KEY;
    }

    /**
     * map 记录点赞数的增量
     */
    public static String commentLikeCountKey() {
        return APP + COMMENT_LIKE_COUNT_KEY;
    }

    /**
     * set 记录点赞的用户
     */
    public static String commentLikeStatusKey(Long commentId) {
        return APP + COMMENT_LIKE_STATUS_KEY + commentId;
    }

    /**
     * set
     */
    public static String submissionVoteRecentKey() {
        return APP + SUBMISSION_VOTE_RECENT_KEY;
    }

    /**
     * map 记录点赞数的增量
     */
    public static String submissionVoteCountKey() {
        return APP + SUBMISSION_VOTE_COUNT_KEY;
    }

    /**
     * map 记录当天用户给比赛投票票数
     * competitionId -> (userId, count)
     */
    public static String competitionVoteStatusKey(Long competitionId) {
        return APP + SUBMISSION_VOTE_STATUS_KEY + competitionId;
    }
    /**
     * 用于清除该key的无参方法
     */
    public static String competitionVoteStatusKey() {
        return APP + SUBMISSION_VOTE_STATUS_KEY;
    }
}
