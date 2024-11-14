/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.goodt.vkpht.module.notification.api.dto.monitor;

import com.fasterxml.jackson.annotation.JsonSetter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

public class EventAchievementDto {
    private Long kpId;
    private Long assignmentId;
    private String authorName;
    private String authorImage;

    public EventAchievementDto() {

    }

    public EventAchievementDto(Long kpId, Long assignmentId, String authorName, String authorImage) {
        this.kpId = kpId;
        this.assignmentId = assignmentId;
        this.authorName = authorName;
        this.authorImage = authorImage;
    }

    public Long getKpId() {
        return kpId;
    }

    @JsonSetter(DtoTagConstants.KR_ID_TAG)
    public void setKpId(Long kpId) {
        this.kpId = kpId;
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
}
