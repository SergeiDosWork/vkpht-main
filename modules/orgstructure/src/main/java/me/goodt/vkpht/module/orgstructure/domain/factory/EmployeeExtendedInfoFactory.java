package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeExtendedInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PersonDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

@UtilityClass
public class EmployeeExtendedInfoFactory {

    public static EmployeeExtendedInfoDto create(EmployeeInfoDto employee,
                                                 PersonDto person,
                                                 List<PositionAssignmentDto> positionAssignments) {
        return new EmployeeExtendedInfoDto(employee, positionAssignments, person);
    }

}
