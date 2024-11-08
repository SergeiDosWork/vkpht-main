package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DivisionTeamRoleShortDto {

    private Long id;
    private Long divisionTeamId;
    private Long roleId;

    private RoleDto role;
    private String externalId;
    private DivisionTeamShortDto divisionTeam;

    public DivisionTeamRoleShortDto(Long id, Long divisionTeamId, Long roleId, String externalId) {
        this.id = id;
        this.divisionTeamId = divisionTeamId;
        setRoleId(roleId);
        this.externalId = externalId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
        this.role = new RoleDto();
        role.setId(roleId);
    }
}
