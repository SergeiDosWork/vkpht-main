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
public class PositionSuccessorDto {
    private Long id;
    private Date dateCommitHr;
    private Date datePriority;
    private EmployeeInfoDto employee;
    private PositionDto position;
    private PositionGroupDto positionGroup;
    private ReasonDto reasonInclusion;
    private ReasonDto reasonExclusion;
    private String commentInclusion;
    private String commentExclusion;
    private String documentUrlInclusion;
    private String documentUrlExclusion;
    private Date dateFrom;
    private Date dateTo;
}
