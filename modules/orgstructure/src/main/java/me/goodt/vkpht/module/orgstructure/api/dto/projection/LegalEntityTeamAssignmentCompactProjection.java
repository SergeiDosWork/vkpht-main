package me.goodt.vkpht.module.orgstructure.api.dto.projection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LegalEntityTeamAssignmentCompactProjection {
    private Long id;
    private Long legalEntityTeamId;
    private Integer systemRoleId;
    private String fullName;
    private String shortName;
}
