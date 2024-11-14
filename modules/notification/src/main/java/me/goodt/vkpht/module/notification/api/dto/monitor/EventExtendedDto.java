/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.goodt.vkpht.module.notification.api.dto.monitor;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

public class EventExtendedDto extends EventDto {
    private List<TaskEstimationMarkDto> taskEstimationMarks;

    public EventExtendedDto() {
    }

    public EventExtendedDto(EventDto event, List<TaskEstimationMarkDto> taskEstimationMarks) {
        super(event.getId(), event.getAssignmentId(), event.getAuthorName(), event.getAuthorImage(),
            event.getBody(), event.getEventType(), event.getEventStatus(), event.getEmployeeFirstName(),
            event.getEmployeeLastName(), event.getDevelopmentFormName(), event.getPollId());
        this.taskEstimationMarks = taskEstimationMarks;
    }

    public List<TaskEstimationMarkDto> getTaskEstimationMarks() {
        return taskEstimationMarks;
    }

    @JsonSetter(DtoTagConstants.TASK_ESTIMATION_MARKS)
    public void setTaskEstimationMarks(List<TaskEstimationMarkDto> taskEstimationMarks) {
        this.taskEstimationMarks = taskEstimationMarks;
    }
}
