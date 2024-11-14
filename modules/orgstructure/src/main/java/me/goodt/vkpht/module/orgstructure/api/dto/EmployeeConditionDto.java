package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeConditionDto {
    private Long id;
    private Long employeeId;
    private Long statusId;
    private Date dateFrom;
    private Date dateTo;
    private String externalId;
}
