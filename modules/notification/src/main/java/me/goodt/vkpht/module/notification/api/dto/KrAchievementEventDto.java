package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class KrAchievementEventDto {
	private Long krId;
	private Long assignmentId;
	private String authorName;
	private String authorImage;
	private List<Long> competenceId;
	private Long processId;

	public KrAchievementEventDto() {

	}

	public KrAchievementEventDto(Long krId, Long assignmentId, String authorName, String authorImage, List<Long> competenceId, Long processId) {
		this.krId = krId;
		this.assignmentId = assignmentId;
		this.authorName = authorName;
		this.authorImage = authorImage;
		this.competenceId = competenceId;
		this.processId = processId;
	}

	public Long getKrId() {
		return krId;
	}

	@JsonSetter(DtoTagConstants.KR_ID_TAG)
	public void setKrId(Long krId) {
		this.krId = krId;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	@JsonSetter(DtoTagConstants.ASSIGNMENT_ID_TAG)
	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getAuthorName() {
		return authorName;
	}

	@JsonSetter(DtoTagConstants.AUTHOR_NAME_TAG)
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorImage() {
		return authorImage;
	}

	@JsonSetter(DtoTagConstants.AUTHOR_IMAGE_TAG)
	public void setAuthorImage(String authorImage) {
		this.authorImage = authorImage;
	}

	public List<Long> getCompetenceId() {
		return competenceId;
	}

	@JsonSetter(DtoTagConstants.COMPETENCE_ID_TAG)
	public void setCompetenceId(List<Long> competenceId) {
		this.competenceId = competenceId;
	}

	public Long getProcessId() {
		return processId;
	}

	@JsonSetter(DtoTagConstants.PROCESS_ID_TAG)
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
}
