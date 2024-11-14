package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DivisionTeamAssignmentShortDto {
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
    private DivisionTeamRoleShortDto divisionTeamRole;
    private DivisionTeamShortDto divisionTeam;

    public DivisionTeamAssignmentShortDto(Long id, EmployeeInfoDto employee, String fullName, String shortName, String abbreviation,
                                          Date dateFrom, Date dateTo, AssignmentTypeDto type, AssignmentStatusDto status,
                                          List<DivisionTeamAssignmentRotationShortDto> divisionTeamAssignmentRotations, String externalId) {
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
    }
}
