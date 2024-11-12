package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;

@Getter
@Setter
@RequiredArgsConstructor
public class EmployeeSearchResult {
    private final Boolean searchStatus;
    private final EmployeeEntity employee;
    private final Long divisionTeamId;
    private final Long divisionTeamAssignmentId;
}
