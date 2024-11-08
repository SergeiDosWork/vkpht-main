package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDeputyDto extends AbstractRes<EmployeeDeputyDto> {
    private Long id;
    private Long employeeViceId;
    private Long employeeSubstituteId;
    private Date dateFrom;
    private Date dateTo;
}
