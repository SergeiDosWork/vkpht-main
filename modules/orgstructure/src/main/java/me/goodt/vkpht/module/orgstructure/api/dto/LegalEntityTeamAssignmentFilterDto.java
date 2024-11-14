package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LegalEntityTeamAssignmentFilterDto {

    private Long legalEntityTeamAssignmentId;

    private Long employeeId;

    private String externalEmployeeId;

    private Long legalEntityTeamId;
}
