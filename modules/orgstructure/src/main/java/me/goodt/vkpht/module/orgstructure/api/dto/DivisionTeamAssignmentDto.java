package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DivisionTeamAssignmentDto {

    private Long id;
    private EmployeeInfoDto employee;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private Date dateFrom;
    private Date dateTo;
    private AssignmentTypeDto type;
    private AssignmentStatusDto status;
    private List<DivisionTeamAssignmentRotationShortDto> divisionTeamAssignmentRotations;
    private String externalId;
    private DivisionTeamRoleDto divisionTeamRole;
    private DivisionTeamDto divisionTeam;

    public DivisionTeamAssignmentDto(Long id, EmployeeInfoDto employee, DivisionTeamRoleDto divisionTeamRole, String fullName,
            String shortName, String abbreviation, Date dateFrom, Date dateTo,
            AssignmentTypeDto type, AssignmentStatusDto status,
            List<DivisionTeamAssignmentRotationShortDto> divisionTeamAssignmentRotations, String externalId,
            DivisionTeamDto divisionTeam) {
        this.id = id;
        this.employee = employee;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.type = type;
        this.status = status;
        this.divisionTeamAssignmentRotations = divisionTeamAssignmentRotations;
        this.externalId = externalId;
        this.divisionTeamRole = divisionTeamRole;
        this.divisionTeam = divisionTeam;
    }
}
