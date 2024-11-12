/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.goodt.vkpht.module.notification.api.dto.monitor;

import com.fasterxml.jackson.annotation.JsonSetter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

public class EventDto {
	private Long id;
	private Long assignmentId;
	private String authorName;
	private String authorImage;
	private String body;
	private EventTypeDto eventType;
	private EventStatusDto eventStatus;
	private String employeeFirstName;
	private String employeeLastName;
	private String developmentFormName;
	private Long pollId;

	public EventDto() {
	}

	public EventDto(Long id, Long assignmentId, String authorName, String authorImage, String body,
					EventTypeDto eventType, EventStatusDto eventStatus, String employeeFirstName,
					String employeeLastName, String developmentFormName, Long pollId) {
		this.id = id;
		this.assignmentId = assignmentId;
		this.authorName = authorName;
		this.authorImage = authorImage;
		this.body = body;
		this.eventType = eventType;
		this.eventStatus = eventStatus;
		this.employeeFirstName = employeeFirstName;
		this.employeeLastName = employeeLastName;
		this.developmentFormName = developmentFormName;
		this.pollId = pollId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public EventTypeDto getEventType() {
		return eventType;
	}

	@JsonSetter(DtoTagConstants.EVENT_TYPE_TAG)
	public void setEventType(EventTypeDto eventType) {
		this.eventType = eventType;
	}

	public EventStatusDto getEventStatus() {
		return eventStatus;
	}

	@JsonSetter(DtoTagConstants.EVENT_STATUS_TAG)
	public void setEventStatus(EventStatusDto eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	@JsonSetter(DtoTagConstants.EMPLOYEE_FIRST_NAME)
	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	@JsonSetter(DtoTagConstants.EMPLOYEE_LAST_NAME)
	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getDevelopmentFormName() {
		return developmentFormName;
	}

	@JsonSetter(DtoTagConstants.DEVELOPMENT_FORM_NAME)
	public void setDevelopmentFormName(String developmentFormName) {
		this.developmentFormName = developmentFormName;
	}

	public Long getPollId() {
		return pollId;
	}

	@JsonSetter(DtoTagConstants.POLL_ID_TAG)
	public void setPollId(Long pollId) {
		this.pollId = pollId;
	}
}
