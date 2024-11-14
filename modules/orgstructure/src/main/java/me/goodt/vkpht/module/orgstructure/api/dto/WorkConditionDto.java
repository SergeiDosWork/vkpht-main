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
public class WorkConditionDto {
    private Long id;
    private Long employeeId;
    private Date dateFrom;
    private Date dateTo;
    private float salary;
    private Long workWeekTypeId;
    private Integer isHasWatch;
    private Integer isIrregularHours;
    private Integer workDurationMinutes;
    private Integer breakDurationMinutes;
    private float shiftCount;
    private Long personnelDocumentId;
}
