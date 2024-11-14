package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskFieldTypeDto {
    private Long id;
    private String name;
    private String params;
    private String description;
    @JsonSetter(DtoTagConstants.IS_REQUIRED_TAG)
    private Integer isRequired;
    @JsonSetter(DtoTagConstants.TYPE_FIELD_GROUP)
    private TaskTypeFieldGroupDto fieldGroup;
    private Long index;
    @JsonSetter(DtoTagConstants.DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DtoTagConstants.DATE_TO_TAG)
    private Date dateTo;
    @JsonSetter(DtoTagConstants.IS_HIDDEN_TAG)
    private Boolean isHidden;
    @JsonSetter(DtoTagConstants.IS_FINAL_TAG)
    private Boolean isFinal;
    @JsonSetter(DtoTagConstants.COMPONENT_FIELD_TAG)
    private ComponentFieldDto componentField;
    @JsonSetter(DtoTagConstants.FIELD_TYPE_CODE_TAG)
    private String fieldTypeCode;
    @JsonSetter(DtoTagConstants.TASK_TYPE_ID_TAG)
    private Long taskTypeId;
    @JsonSetter(DtoTagConstants.IS_SYSTEM_VISIBLE_TAG)
    private Boolean isSystemVisible;
    @JsonSetter(DtoTagConstants.IS_UNIQUE_TAG)
    private Boolean isUnique;
    @JsonSetter(DtoTagConstants.IS_SYSTEM_TAG)
    private boolean isSystem;
    @JsonSetter(DtoTagConstants.IS_EDITABLE_IF_SYSTEM_TAG)
    private boolean isEditableIfSystem;
}
