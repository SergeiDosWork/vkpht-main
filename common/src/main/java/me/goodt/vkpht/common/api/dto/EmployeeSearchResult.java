package me.goodt.vkpht.common.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.goodt.drive.rtcore.model.orgstructure.entities.EmployeeEntity;

@Getter
@Setter
@RequiredArgsConstructor
public class EmployeeSearchResult {
    private final Boolean searchStatus;
    private final EmployeeEntity employee;
    private final Long divisionTeamId;
    private final Long divisionTeamAssignmentId;
}
