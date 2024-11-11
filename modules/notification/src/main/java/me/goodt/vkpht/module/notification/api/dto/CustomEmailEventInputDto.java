package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class CustomEmailEventInputDto {
	@JsonSetter(DtoTagConstants.DIVISION_TEAM_ASSIGNMENT_ID_TAG)
	private Long assignmentId;
	@JsonSetter(DtoTagConstants.MESSAGE_TYPE_TAG)
	private Integer messageType;
	private String title;
	private String body;
	@JsonSetter(DtoTagConstants.TASK_LINK_TYPE_ID_TAG)
	private Long taskLinkTypeId;
	@JsonSetter(DtoTagConstants.TYPE_ID_TAG)
	private Long typeId;
	@JsonSetter(DtoTagConstants.TASK_ID_TO_TAG)
	private Long taskIdTo;
}
