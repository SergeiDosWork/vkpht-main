package me.goodt.vkpht.module.notification.api.dto.monitor;

import com.fasterxml.jackson.annotation.JsonSetter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

public class ShortEventDto {
    private Long id;
    private Long assignmentId;
    private String authorName;
    private String authorImage;
    private String body;
    private Long eventTypeId;
    private Long eventStatusId;
    private Long processId;

    public ShortEventDto() {

    }

    public ShortEventDto(Long id, Long assignmentId, String authorName, String authorImage,
                         String body, Long eventTypeId, Long eventStatusId, Long processId) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.authorName = authorName;
        this.authorImage = authorImage;
        this.body = body;
        this.eventTypeId = eventTypeId;
        this.eventStatusId = eventStatusId;
        this.processId = processId;
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

    public Long getEventTypeId() {
        return eventTypeId;
    }

    @JsonSetter(DtoTagConstants.EVENT_TYPE_ID_TAG)
    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Long getEventStatusId() {
        return eventStatusId;
    }

    @JsonSetter(DtoTagConstants.EVENT_STATUS_ID_TAG)
    public void setEventStatusId(Long eventStatusId) {
        this.eventStatusId = eventStatusId;
    }

    public Long getProcessId() {
        return processId;
    }

    @JsonSetter(DtoTagConstants.PROCESS_ID_TAG)
    public void setProcessId(Long processId) {
        this.processId = processId;
    }
}
