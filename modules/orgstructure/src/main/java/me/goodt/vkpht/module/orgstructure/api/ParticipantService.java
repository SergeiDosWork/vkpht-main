package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import com.goodt.drive.rtcore.dto.tasksetting.ParticipantDto;

public interface ParticipantService {
    List<ParticipantDto> getParticipants(Long employeeId, Long evaluationEventId);
}
