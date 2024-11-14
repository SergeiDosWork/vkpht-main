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
public class DivisionInfoDto {
    private DivisionDto division;
    private EmployeeExtendedInfoDto head;
    private List<DivisionTeamDto> divisionTeams;
}
