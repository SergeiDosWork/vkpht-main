package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionTeamRoleRawDto {
    private Long id;
    private Long divisionTeamId;
    private Long roleId;
    private Integer positionImportanceId;
    private String externalId;
}
