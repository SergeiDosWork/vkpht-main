package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.EMPLOYEE_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.PARENT_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.STATUS_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_ID_TAG;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistoryDto {
    private Long id;
    private Date date;
    @JsonSetter(TASK_ID_TAG)
    private Long taskId;
    @JsonSetter(PARENT_ID_TAG)
    private Long parentId;
    @JsonSetter(STATUS_ID_TAG)
    private Long statusId;
    private StatusDto status;
    private String comment;
    @JsonSetter(EMPLOYEE_ID_TAG)
    private Long employeeId;
}
