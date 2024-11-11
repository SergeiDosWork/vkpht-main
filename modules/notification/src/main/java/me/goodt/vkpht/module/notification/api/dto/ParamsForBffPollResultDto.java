package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

public class ParamsForBffPollResultDto {

	private Long taskId;
	private Long employeeId;
	private Long indicatorLevelId;
	private Long competenceId;
	private Long scaleLevelId;
	private String comment;

	public ParamsForBffPollResultDto() {
	}

	public ParamsForBffPollResultDto(Long taskId, Long employeeId, Long indicatorLevelId, Long competenceId, Long scaleLevelId, String comment) {
		this.taskId = taskId;
		this.employeeId = employeeId;
		this.indicatorLevelId = indicatorLevelId;
		this.competenceId = competenceId;
		this.scaleLevelId = scaleLevelId;
		this.comment = comment;
	}

	public Long getTaskId() {
		return taskId;
	}

	@JsonSetter(DtoTagConstants.TASK_ID_TAG)
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	@JsonSetter(DtoTagConstants.EMPLOYEE_ID_TAG)
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getIndicatorLevelId() {
		return indicatorLevelId;
	}

	@JsonSetter(DtoTagConstants.INDICATOR_LEVEL_ID_TAG)
	public void setIndicatorLevelId(Long indicatorLevelId) {
		this.indicatorLevelId = indicatorLevelId;
	}

	public Long getCompetenceId() {
		return competenceId;
	}

	@JsonSetter(DtoTagConstants.COMPETENCE_ID_TAG)
	public void setCompetenceId(Long competenceId) {
		this.competenceId = competenceId;
	}

	public Long getScaleLevelId() {
		return scaleLevelId;
	}

	@JsonSetter(DtoTagConstants.SCALE_LEVEL_ID_TAG)
	public void setScaleLevelId(Long scaleLevelId) {
		this.scaleLevelId = scaleLevelId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
