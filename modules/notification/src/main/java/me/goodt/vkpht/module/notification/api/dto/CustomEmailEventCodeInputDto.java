package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomEmailEventCodeInputDto {
    private String code;
    @JsonSetter(me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.COMP_ID_TAG)
    private List<Long> competenceIds;
    @JsonSetter(me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_ID_TAG)
    private Long taskId;
    private String comment;
    @JsonSetter(DtoTagConstants.DIVISION_TEAM_SUCCESSOR_ID_TAG)
    private Long divisionTeamSuccessorId;
}
