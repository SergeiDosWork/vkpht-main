package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskAchievementDto {
	@JsonSetter(DtoTagConstants.COMPETENCE_ID_TAG)
	private List<Long> competenceId;
	@JsonSetter(DtoTagConstants.TASK_ID_TAG)
	private Long taskId;
	@JsonSetter(DtoTagConstants.ASSIGNMENT_ID_TAG)
	private Long assignmentId;
	@JsonSetter(DtoTagConstants.AUTHOR_NAME_TAG)
	private String authorName;
	@JsonSetter(DtoTagConstants.AUTHOR_IMAGE_TAG)
	private String authorImage;
	@JsonSetter(DtoTagConstants.PROCESS_ID_TAG)
	private Long processId;
}
