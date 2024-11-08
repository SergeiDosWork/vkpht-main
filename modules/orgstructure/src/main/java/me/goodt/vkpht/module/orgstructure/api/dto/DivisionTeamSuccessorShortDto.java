package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionTeamSuccessorShortDto {
    private Long id;
    private Date dateFrom;
    private Date dateTo;
    private Date dateCommitHr;
    private EmployeeInfoDto employee;
    private Date datePriority;
    private Integer reasonIdInclusion;
    private Integer reasonIdExclusion;
    private String commentInclusion;
    private String commentExclusion;
    private String documentUrlInclusion;
    private String documentUrlExclusion;
    private List<DivisionTeamSuccessorReadinessDto> divisionTeamSuccessorReadiness;
    private DivisionTeamDto divisionTeam;
}
