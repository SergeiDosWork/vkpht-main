package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.Setter;

import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentShort;

@Getter
@Setter
//@Accessors(chain = true)
public class DivisionAssignmentRoleDto {
    
    private Integer systemRoleId;

    private Long divisionTeamId;

    private DivisionTeamAssignmentShort assignment;
}
