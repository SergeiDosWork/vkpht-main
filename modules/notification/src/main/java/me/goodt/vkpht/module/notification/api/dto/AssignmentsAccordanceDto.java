package me.goodt.vkpht.module.notification.api.dto;

public class AssignmentsAccordanceDto {

	private Long[] assignmentIds;
	private Float percent;

	public AssignmentsAccordanceDto() {
	}

	public AssignmentsAccordanceDto(Long[] assignmentIds, Float percent) {
		this.assignmentIds = assignmentIds;
		this.percent = percent;
	}

	public Long[] getAssignmentIds() {
		return assignmentIds;
	}

	public void setAssignmentIds(Long[] assignmentIds) {
		this.assignmentIds = assignmentIds;
	}

	public Float getPercent() {
		return percent;
	}

	public void setPercent(Float percent) {
		this.percent = percent;
	}
}
