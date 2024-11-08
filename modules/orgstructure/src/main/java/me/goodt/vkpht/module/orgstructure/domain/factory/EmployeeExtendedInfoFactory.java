package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeExtendedInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

@UtilityClass
public class EmployeeExtendedInfoFactory {

    public static EmployeeExtendedInfoDto create(EmployeeEntity entity, List<PositionAssignmentEntity> positionAssignments) {
        List<PositionAssignmentDto> assignmentDtos = new ArrayList<>();

        for (PositionAssignmentEntity assignment : positionAssignments) {
            assignmentDtos.add(PositionAssignmentFactory.create(assignment));
        }

        return new EmployeeExtendedInfoDto(EmployeeInfoFactory.create(entity), assignmentDtos, entity.getPerson());
    }

}
