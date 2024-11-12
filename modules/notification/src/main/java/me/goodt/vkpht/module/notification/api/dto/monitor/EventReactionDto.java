/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.goodt.vkpht.module.notification.api.dto.monitor;

import com.fasterxml.jackson.annotation.JsonSetter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

public class EventReactionDto {
	private Long id;
	private Long employeeId;
	private Long eventReactionTypeId;
	private Long eventId;
	private String reaction;

	public EventReactionDto() {

	}

	public EventReactionDto(Long id, Long employeeId, Long eventReactionTypeId, Long eventId, String reaction) {
		this.id = id;
		this.employeeId = employeeId;
		this.eventReactionTypeId = eventReactionTypeId;
		this.eventId = eventId;
		this.reaction = reaction;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	@JsonSetter(DtoTagConstants.EMPLOYEE_ID_TAG)
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getEventReactionTypeId() {
		return eventReactionTypeId;
	}

	@JsonSetter(DtoTagConstants.EVENT_REACTION_TYPE_ID_TAG)
	public void setEventReactionTypeId(Long eventReactionTypeId) {
		this.eventReactionTypeId = eventReactionTypeId;
	}

	public Long getEventId() {
		return eventId;
	}

	@JsonSetter(DtoTagConstants.EVENT_ID_TAG)
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}
}
