package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

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
public class ModuleDto {
    private String code;
    private String name;
    private String description;
    @JsonSetter(DtoTagConstants.IS_ADMINABLE_TAG)
    private Integer isAdminable;
    private String externalId;
    @JsonSetter(DtoTagConstants.DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DtoTagConstants.DATE_TO_TAG)
    private Date dateTo;
    @JsonSetter(DtoTagConstants.AUTHOR_EMPLOYEE_ID_TAG)
    private Long authorEmployeeId;
    @JsonSetter("update_employee_id")
    private Long updateEmployeeId;
    @JsonSetter("update_date")
    private Date updateDate;
    @JsonSetter(DtoTagConstants.IS_SYSTEM_TAG)
    private boolean isSystem;
    @JsonSetter(DtoTagConstants.IS_EDITABLE_IF_SYSTEM_TAG)
    private boolean isEditableIfSystem;
}
