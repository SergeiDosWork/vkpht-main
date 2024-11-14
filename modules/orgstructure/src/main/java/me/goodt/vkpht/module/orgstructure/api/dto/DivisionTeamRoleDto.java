package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionTeamRoleDto {
    private Long id;
    private DivisionTeamDto divisionTeam;
    private RoleDto role;
    private PositionImportanceDto positionImportance;
    private String externalId;
}
