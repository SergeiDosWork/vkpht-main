package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionTeamWithRolesAndSuccessorsStatDto {
    private Long id;
    private List<DivisionTeamRoleAndImportanceStatDto> divisionTeamRoles;
    private DivisionTeamSuccessorsStatDto divisionTeamSuccessors;
    private Float security;
}
