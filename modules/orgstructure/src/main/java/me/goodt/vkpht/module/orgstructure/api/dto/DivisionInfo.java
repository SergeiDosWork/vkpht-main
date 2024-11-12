package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionInfo {
    private DivisionEntity division;
    private EmployeeEntity divisionHead;
    private PositionAssignmentInfo divisionHeadInfo;
}
