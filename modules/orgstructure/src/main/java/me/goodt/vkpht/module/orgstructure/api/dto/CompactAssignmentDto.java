package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompactAssignmentDto {

    private Long id;

    private Long employeeId;

    private Long divisionId;

    private Integer systemRoleId;
}
