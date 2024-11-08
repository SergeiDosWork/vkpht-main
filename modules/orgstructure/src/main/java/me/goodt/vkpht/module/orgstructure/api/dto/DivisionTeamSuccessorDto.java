package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DivisionTeamSuccessorDto extends DivisionTeamSuccessorShortDto {

    public DivisionTeamSuccessorDto(Long id, Date dateFrom, Date dateTo, Date dateCommitHr, EmployeeInfoDto employee,
                                    Date datePriority, Integer reasonIdInclusion, Integer reasonIdExclusion,
                                    String commentInclusion, String commentExclusion, String documentUrlInclusion, String documentUrlExclusion,
                                    List<DivisionTeamSuccessorReadinessDto> divisionTeamSuccessorReadiness,
                                    DivisionTeamDto divisionTeam, DivisionTeamRoleDto divisionTeamRole) {
        super(id, dateFrom, dateTo, dateCommitHr, employee, datePriority, reasonIdInclusion, reasonIdExclusion, commentInclusion, commentExclusion,
              documentUrlInclusion, documentUrlExclusion, divisionTeamSuccessorReadiness, divisionTeam);
        this.divisionTeamRole = divisionTeamRole;
    }
    private DivisionTeamRoleDto divisionTeamRole;
}
