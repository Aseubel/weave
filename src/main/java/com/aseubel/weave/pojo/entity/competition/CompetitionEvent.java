package com.aseubel.weave.pojo.entity.competition;

import com.aseubel.weave.common.GenericEnumConverter;
import com.aseubel.weave.common.constants.BaseEnum;
import jakarta.persistence.Converter;

import java.util.Optional;

/**
 * @author Aseubel
 * @date 2025/7/28 下午5:07
 */
public enum CompetitionEvent implements BaseEnum<CompetitionEvent> {
    START(1, "开始比赛"),
    PUBLIC_VOTING(2, "公开投票"),
    HIDE_VOTING(3, "隐藏投票"),
    END(4, "终止比赛"),
    MODIFY_INFO(5, "修改比赛信息"),
    STOP_VOTING(6, "停止投票"),
    PUBLISH_announcement(7, "发布公告");

    private final Integer code;
    private final String name;

    CompetitionEvent(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static Optional<CompetitionState> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(CompetitionState.class, code));
    }

    @Converter(autoApply = true)
    public static class CompetitionEventConverter
            extends GenericEnumConverter<CompetitionState> {
        // 空实现即可，自动继承父类逻辑
    }
}
