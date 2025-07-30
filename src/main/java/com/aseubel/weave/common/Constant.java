package com.aseubel.weave.common;

/**
 * @author Aseubel
 * @date 2025/6/28 下午9:28
 */
public class Constant {
    // 短信验证码有效期
    public static final int SMS_CODE_EXPIRE_MINUTES = 5;
    // 权限认证相关
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    // 阿里云OSS相关
    public static final String ENDPOINT = "oss-cn-guangzhou.aliyuncs.com";
    public static final String BUCKET_NAME = "dgut-weave";  // 填写Bucket名称，例如examplebucket。
    public static final String REGION = "cn-guangzhou"; // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
    public static final long FILE_PART_SIZE = 1 * 1024 * 1024L;
    public static final String APP = "dgut-weave";

    // 状态机相关
    public static final String START_TRANSITION = "start";
    public static final String END_TRANSITION = "end";
    public static final String PUBLIC_VOTING_TRANSITION = "public_voting";
    public static final String HIDE_VOTING_TRANSITION = "hide_voting";
    public static final String MODIFY_INFO_TRANSITION = "modify_info";
    public static final String STOP_VOTING_TRANSITION = "stop_voting";
    public static final String ANNOUNCE_TRANSITION = "announce";
}
