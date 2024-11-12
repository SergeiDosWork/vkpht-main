package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskFieldDto {
    private Long id;
    private TaskFieldTypeDto type;
    private String value;
    @JsonSetter(DtoTagConstants.EXTERNAL_ID_TAG)
    private String externalId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSetter(DtoTagConstants.DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DtoTagConstants.DATE_TO_TAG)
    private Date dateTo;
    @JsonSetter(DtoTagConstants.TASK_ID_TAG)
    private Long taskId;
    @JsonSetter(DtoTagConstants.AUTHOR_EMPLOYEE_ID_TAG)
    private Long authorEmployeeId;
    @JsonSetter("update_employee_id")
    private Long updateEmployeeId;
    @JsonSetter("update_date")
    private Date updateDate;
    @JsonSetter(DtoTagConstants.IS_WRITE_RESTRICTION_TAG)
    private Boolean isWriteRestriction;
}
