package com.aseubel.weave.pojo.dto.competition;

import cn.hutool.core.util.BooleanUtil;
import com.aseubel.weave.pojo.entity.competition.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025/7/30 下午4:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantResponse {

    private Long userId;
    private Long participantId;
    private String name;
    private String profile;
    private Boolean publicAccount;

    public static ParticipantResponse fromParticipant(Participant participant) {
        return ParticipantResponse.builder()
                .userId(participant.getUser().getId())
                .participantId(participant.getId())
                .name(participant.getName())
                .profile(participant.getProfile())
                .publicAccount(participant.getPublicAccount())
                .build();
    }

    public void hide() {
        if (BooleanUtil.isTrue(publicAccount)) {
            this.userId = null;
        }
    }
}
