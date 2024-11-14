package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    @JsonSetter(PARENT_ID_TAG)
    private Long parentId;
    @JsonSetter(EXTERNAL_ID_TAG)
    private String externalId;
    @JsonSetter(DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DATE_TO_TAG)
    private Date dateTo;
    private TaskTypeShort2Dto type;
    private StatusDto status;
    private String comment;
    @JsonSetter(USER_ID_TAG)
    private Long userId;
    private List<TaskFieldDto> fields;
    private RoleInfoDto role;
    @JsonSetter(AUTHOR_EMPLOYEE_ID_TAG)
    private Long authorEmployeeId;
    @JsonSetter("update_employee_id")
    private Long updateEmployeeId;
    @JsonSetter("update_date")
    private Date updateDate;
    @JsonSetter(ROOT_ID_TAG)
    private Long rootId;
    @JsonSetter(TASK_TYPE_ROLES_TAG)
    private List<TaskTypeRoleForTaskDto> taskTypeRoles;
}
