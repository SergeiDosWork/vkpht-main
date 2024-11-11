package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessorNotificateByCodeInputData {
	private String code;
	@JsonSetter(DtoTagConstants.DIVISION_TEAM_SUCCESSOR_ID_TAG)
	private Long divisionTeamSuccessorId;
}
