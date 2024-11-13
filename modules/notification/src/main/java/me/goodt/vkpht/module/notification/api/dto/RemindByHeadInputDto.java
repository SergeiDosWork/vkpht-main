package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RemindByHeadInputDto {
    @JsonSetter(DtoTagConstants.POLL_ID_TAG)
    private Long pollId;
    @JsonSetter(DtoTagConstants.DIVISION_TEAM_ASSIGNMENT_ID_TAG)
    private List<Long> assignments;
}
