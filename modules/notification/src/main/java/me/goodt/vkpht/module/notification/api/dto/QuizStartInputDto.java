package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class QuizStartInputDto {
	@JsonSetter(DtoTagConstants.USER_POLL_ID_TAG)
	private Long userPollId;
	@JsonSetter(DtoTagConstants.EMPLOYEE_ID_TAG)
	private Long employeeId;
	@JsonSetter(DtoTagConstants.POLL_ID_TAG)
	private Long pollId;
}
