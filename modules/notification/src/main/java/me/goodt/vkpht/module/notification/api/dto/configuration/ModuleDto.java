package me.goodt.vkpht.module.notification.api.dto.configuration;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.AUTHOR_EMPLOYEE_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.IS_ENABLED_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.IS_SYSTEM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.UPDATE_DATE_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.UPDATE_EMPLOYEE_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.VERSION_CODE_EXCLUSION_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.VERSION_CODE_INCLUSION_TAG;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDto {

    private Long id;

    @JsonSetter(DATE_FROM_TAG)
    private Date dateFrom;

    @JsonSetter(UPDATE_DATE_TAG)
    private Date updateDate;

    private String code;

    private String name;

    private String description;

    @JsonSetter(IS_ENABLED_TAG)
    private Boolean isEnabled;

    @JsonSetter(VERSION_CODE_INCLUSION_TAG)
    private String inclusion;

    @JsonSetter(VERSION_CODE_EXCLUSION_TAG)
    private String exclusion;

    @JsonSetter(AUTHOR_EMPLOYEE_TAG)
    private Long authorEmployeeId;

    @JsonSetter(UPDATE_EMPLOYEE_TAG)
    private Long updateEmployeeId;

    @JsonSetter(IS_SYSTEM_TAG)
    private Boolean isSystem;
}
