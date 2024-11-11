package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class QuizFinishInputDto {
	@JsonSetter(DtoTagConstants.USER_POLL_ID_TAG)
	private Long userPollId;
	@JsonSetter(DtoTagConstants.PASSED_QUIZ_DTA_ID_TAG)
	private Long passedQuizDtaId;
}
