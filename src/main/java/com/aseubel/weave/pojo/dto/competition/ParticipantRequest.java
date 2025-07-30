package com.aseubel.weave.pojo.dto.competition;

import com.aseubel.weave.pojo.entity.competition.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/7/29 下午3:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantRequest implements Serializable {
    private Long participantId;
    private String name;
    private String profile;
    private Boolean publicAccount;

    public Participant toEntity() {
        Participant participant = new Participant();
        participant.setId(participantId);
        participant.setName(name);
        participant.setProfile(profile);
        participant.setPublicAccount(publicAccount);
        return participant;
    }
}
