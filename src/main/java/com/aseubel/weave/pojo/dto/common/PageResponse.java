package com.aseubel.weave.pojo.dto.common;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * 数据列表
     */
    private List<T> content;

    /**
     * 当前页码（从1开始）
     */
    private Integer page;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否为第一页
     */
    private Boolean isFirst;

    /**
     * 是否为最后一页
     */
    private Boolean isLast;

    /**
     * 是否为空
     */
    private Boolean isEmpty;

    public static <T> PageResponse<T> of(List<T> content, Integer page, Integer size, Long total) {
        int totalPages = (int) ((total + size - 1) / size); // 更高效的总页数计算

        return PageResponse.<T>builder()
                .content(content)
                .page(page)
                .size(size)
                .total(total)
                .totalPages(totalPages)
                .hasNext(page < totalPages)
                .hasPrevious(page > 1)
                .isFirst(page == 1)
                .isLast(page.equals(totalPages))
                .isEmpty(ObjectUtil.isEmpty(content))
                .build();
    }

}