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
public class DivisionWithDivisionTeamsWithDivisionTeamRolesAndDivisionTeamSuccessorsDto {
    private Long id;
    private List<DivisionTeamWithRolesAndSuccessorsStatDto> divisionTeams;
}
