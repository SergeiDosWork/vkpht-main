package me.goodt.vkpht.module.notification.api.dto.monitor;

import com.fasterxml.jackson.annotation.JsonSetter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

public class EstimationCriteriaDto {

	private Long id;
	private Long code;
	private String name;
	private String description;
	private Long taskTypeId;

	public EstimationCriteriaDto() {
	}

	public EstimationCriteriaDto(Long id, Long code, String name, String description, Long taskTypeId) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.description = description;
		this.taskTypeId = taskTypeId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTaskTypeId() {
		return taskTypeId;
	}

	@JsonSetter(DtoTagConstants.TASK_TYPE_ID_TAG)
	public void setTaskTypeId(Long taskTypeId) {
		this.taskTypeId = taskTypeId;
	}
}
