package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusChangeRuleDto {
    private Long id;
    @JsonProperty(DtoTagConstants.TASK_TYPE_ROLE_ID_TAG)
    private Long taskTypeRoleId;
    @JsonProperty(DtoTagConstants.STATUS_FROM_TAG)
    private StatusDto statusFrom;
    @JsonProperty(DtoTagConstants.STATUS_TO_TAG)
    private StatusDto statusTo;
    @JsonProperty(DtoTagConstants.IS_COMMENTABLE_TAG)
    private Boolean isCommentable;
    @JsonProperty(DtoTagConstants.IS_COMMENT_REQUIRED_TAG)
    private Boolean isCommentRequired;
    private String description;
    private String name;
    @JsonProperty(DtoTagConstants.DATE_EXECUTE_TAG)
    private Date dateExecute;
    @JsonProperty(DtoTagConstants.DATE_FROM_TAG)
    private Date dateFrom;
    @JsonProperty(DtoTagConstants.DATE_TO_TAG)
    private Date dateTo;
    @JsonSetter(DtoTagConstants.IS_SYSTEM_TAG)
    private boolean isSystem;
    @JsonSetter(DtoTagConstants.IS_EDITABLE_IF_SYSTEM_TAG)
    private boolean isEditableIfSystem;
}
