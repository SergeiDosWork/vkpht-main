package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskTypeRolePermissionDto {
    private Long id;
    private String name;
    private StatusDto status;
    @JsonProperty(DtoTagConstants.TASK_TYPE_ROLE_ID_TAG)
    private Long taskTypeRoleId;
    @JsonProperty(DtoTagConstants.IS_READABLE_TAG)
    private Boolean isReadable;
    @JsonProperty(DtoTagConstants.IS_WRITEABLE_TAG)
    private Boolean isWriteable;
    @JsonProperty(DtoTagConstants.IS_CREATABLE_TAG)
    private Boolean isCreatable;
    @JsonProperty(DtoTagConstants.PARENT_TASK_TYPE_ID_TAG)
    private Long parentTaskTypeId;
    @JsonProperty(DtoTagConstants.PARENT_STATUS_ID_TAG)
    private Long parentStatusId;
    @JsonProperty(DtoTagConstants.IS_SYSTEM_TAG)
    private boolean isSystem;
    @JsonProperty(DtoTagConstants.IS_EDITABLE_IF_SYSTEM_TAG)
    private boolean isEditableIfSystem;
}
