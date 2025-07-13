package com.aseubel.weave.pojo.dto.post;

import com.aseubel.weave.common.annotation.FieldDesc;
import com.aseubel.weave.pojo.entity.post.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 帖子请求DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
public class PostRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200字符")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(max = 10000, message = "内容长度不能超过10000字符")
    private String content;

    @NotNull(message = "帖子类型不能为空")
    private Post.PostType type;

    @FieldDesc(name = "图片URL列表")
    private List<String> images;

    @FieldDesc(name = "标签列表")
    private List<String> tags;

    @FieldDesc(name = "兴趣标签ID集合")
    private Set<Long> interestTagIds;

//    @FieldDesc(name = "是否置顶（管理员功能）")
//    private Boolean isTop = false;

    @FieldDesc(name = "帖子状态")
    private Post.PostStatus status = Post.PostStatus.PUBLISHED;
}