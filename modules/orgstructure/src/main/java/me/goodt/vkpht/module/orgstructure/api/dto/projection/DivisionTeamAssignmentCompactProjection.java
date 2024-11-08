package me.goodt.vkpht.module.orgstructure.api.dto.projection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DivisionTeamAssignmentCompactProjection {
    private Long id;
    private Long employeeId;
    private String personLastName;
    private String personFirstName;
    private String personMiddleName;
    private Long divisionTeamId;
    private Long divisionId;
    private Long legalEntityId;
    private Long roleId;
    private Integer systemRoleId;
}
