package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TaskTypeRoleForTaskDto {
    private Long id;
    private String name;
    private String description;
    private Long index;
    private RoleInfoDto role;
    @JsonProperty(DtoTagConstants.TASK_TYPE_TAG)
    private TaskTypeShort2Dto taskType;
    @JsonProperty(DtoTagConstants.COUNT_MIN_TAG)
    private Long countMin;
    @JsonProperty(DtoTagConstants.COUNT_MAX_TAG)
    private Long countMax;
    @JsonProperty(DtoTagConstants.COUNT_RECOMMEND_TAG)
    private Long countRecommend;
    @JsonProperty(DtoTagConstants.IS_AUTO_ASSIGN_TAG)
    private Boolean isAutoAssign;
    @JsonProperty(DtoTagConstants.ASSIGN_TASK_TYPE_ID_TAG)
    private Long assignTaskTypeId;
    @JsonProperty(DtoTagConstants.ASSIGN_SYSTEM_STATUS_ID_TAG)
    private Long assignSystemStatusId;
    @JsonProperty(DtoTagConstants.TASK_TYPE_ROLE_PERMISSIONS_TAG)
    private List<TaskTypeRolePermissionDto> taskTypeRolePermissionDtos;
    @JsonProperty(DtoTagConstants.STATUS_CHANGE_RULES_TAG)
    private List<StatusChangeRuleDto> statusChangeRuleDtos;
    @JsonProperty(DtoTagConstants.IS_SYSTEM_TAG)
    private boolean isSystem;
    @JsonProperty(DtoTagConstants.IS_EDITABLE_IF_SYSTEM_TAG)
    private boolean isEditableIfSystem;
}
