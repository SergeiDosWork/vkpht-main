package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

@UtilityClass
public class EmployeeInfoFactory {

    public static EmployeeInfoDto create(EmployeeEntity entity) {
        if (entity.getPerson() != null) {
            return new EmployeeInfoDto(entity.getId(), entity.getExternalId(),
                                       entity.getPerson().getName(), entity.getPerson().getSurname(),
                                       entity.getPerson().getPatronymic(), entity.getPerson().getPhoto(),
                                       entity.getNumber(), entity.getEmail());
        }
        return new EmployeeInfoDto(entity.getId(), entity.getExternalId(),
                                   null, null, null, null, entity.getNumber(), entity.getEmail());
    }

    public static EmployeeInfoDto createWithJobInfo(EmployeeEntity entity, List<PositionAssignmentEntity> positionAssignmentEntities) {
        List<PositionAssignmentDto> positionAssignmentDtos = positionAssignmentEntities.stream()
                .map(PositionAssignmentFactory::create)
                .collect(Collectors.toList());
        if (entity.getPerson() != null) {
            return new EmployeeInfoDto(entity.getId(), entity.getExternalId(), entity.getPerson().getName(),
                                       entity.getPerson().getSurname(), entity.getPerson().getPatronymic(),
                                       entity.getPerson().getPhoto(), entity.getNumber(), entity.getEmail(), positionAssignmentDtos);
        }
        return new EmployeeInfoDto(entity.getId(), entity.getExternalId(),
                                   null, null, null, null, entity.getNumber(), entity.getEmail(), positionAssignmentDtos);
    }
}
