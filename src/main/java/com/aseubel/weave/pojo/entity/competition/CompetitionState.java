package com.aseubel.weave.pojo.entity.competition;

import com.aseubel.weave.common.GenericEnumConverter;
import com.aseubel.weave.common.constants.BaseEnum;
import jakarta.persistence.Converter;

import java.util.Optional;

/**
 * @author Aseubel
 * @date 2025/7/28 下午4:47
 */
public enum CompetitionState implements BaseEnum<CompetitionState> {
    NOT_STARTED(1, "未开始"),
    PUBLIC_RUNNING(2, "已开始（公开投票信息）"),
    HIDDEN_RUNNING(3, "已开始（隐藏投票信息）"),
    ENDED(4, "已结束");

    private final Integer code;
    private final String name;

    CompetitionState(Integer code, String name) {
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
    public static class CompetitionStateConverter
            extends GenericEnumConverter<CompetitionState> {
        // 空实现即可，自动继承父类逻辑
    }
}
