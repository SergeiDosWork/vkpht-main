package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DivisionTeamRoleContainerDto {

    public DivisionTeamRoleContainerDto(Long id, DivisionTeamDto divisionTeam, RoleDto role, PositionImportanceDto positionImportance,
                                        List<DivisionTeamAssignmentShortDto> divisionTeamAssignmentDtos,
                                        List<DivisionTeamSuccessorShortDto> divisionTeamSuccessorDtos) {
        this.id = id;
        this.divisionTeam = divisionTeam;
        this.role = role;
        this.positionImportance = positionImportance;
        this.divisionTeamAssignmentDtos = divisionTeamAssignmentDtos;
        this.divisionTeamSuccessorDtos = divisionTeamSuccessorDtos;
    }

    private Long id;
    private DivisionTeamDto divisionTeam;
    private RoleDto role;
    private PositionImportanceDto positionImportance;
    private List<DivisionTeamAssignmentShortDto> divisionTeamAssignmentDtos;
    private List<DivisionTeamSuccessorShortDto> divisionTeamSuccessorDtos;
    private List<PositionSuccessorDto> positionSuccessorDtos;
    private List<PositionSuccessorReadinessDto> positionSuccessorReadinessDtos;
}
