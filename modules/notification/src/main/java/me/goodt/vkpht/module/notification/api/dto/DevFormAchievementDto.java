package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFieldDto;

public class DevFormAchievementDto {

    private Long eventTypeId;
    private Long developmentFormId;
    private List<Long> competenceId;
    private List<TaskFieldDto> fields;
    private Long assigmentId;
    private String authorName;
    private String authorImage;
    private Long processId;

    public DevFormAchievementDto() {
    }

    public DevFormAchievementDto(Long eventTypeId, Long developmentFormId, List<Long> competenceId) {
        this.eventTypeId = eventTypeId;
        this.developmentFormId = developmentFormId;
        this.competenceId = competenceId;
    }

    public Long getEventTypeId() {
        return eventTypeId;
    }

    @JsonSetter(DtoTagConstants.EVENT_TYPE_ID_TAG)
    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Long getDevelopmentFormId() {
        return developmentFormId;
    }

    @JsonSetter(DtoTagConstants.DEVELOPMENT_FORM_ID)
    public void setDevelopmentFormId(Long developmentFormId) {
        this.developmentFormId = developmentFormId;
    }

    public List<Long> getCompetenceId() {
        return competenceId;
    }

    @JsonSetter(DtoTagConstants.COMPETENCE_ID_TAG)
    public void setCompetenceId(List<Long> competenceId) {
        this.competenceId = competenceId;
    }

    public List<TaskFieldDto> getFields() {
        return fields;
    }

    @JsonSetter(DtoTagConstants.FIELDS_TAG)
    public void setFields(List<TaskFieldDto> fields) {
        this.fields = fields;
    }

    public Long getAssigmentId() {
        return assigmentId;
    }

    @JsonSetter(DtoTagConstants.ASSIGNMENT_ID_TAG)
    public void setAssigmentId(Long assigmentId) {
        this.assigmentId = assigmentId;
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

    public Long getProcessId() {
        return processId;
    }

    @JsonSetter(DtoTagConstants.PROCESS_ID_TAG)
    public void setProcessId(Long processId) {
        this.processId = processId;
    }
}
