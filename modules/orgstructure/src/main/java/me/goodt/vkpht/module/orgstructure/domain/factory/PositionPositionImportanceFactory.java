package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PersonSimpleDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionImportanceDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionImportanceEntity;

@UtilityClass
public class PositionPositionImportanceFactory {

    public static PositionPositionImportanceDto create(PositionPositionImportanceEntity entity) {

        PersonSimpleDto personSimpleDto = null;
        if (entity.getAuthorEmployee() != null && entity.getAuthorEmployee().getPerson() != null) {
            personSimpleDto = PersonSimpleDto.builder()
                .name(entity.getAuthorEmployee().getPerson().getName())
                .surname(entity.getAuthorEmployee().getPerson().getSurname())
                .patronymic(entity.getAuthorEmployee().getPerson().getPatronymic())
                .build();
        }

        return PositionPositionImportanceDto.builder()
            .id(entity.getId())
            .dateFrom(entity.getDateFrom())
            .dateTo(entity.getDateTo())
            .systemRoleId(entity.getSystemRoleId().getId())
            .person(personSimpleDto)
            .authorEmployeeId(entity.getAuthorEmployee() != null ? entity.getAuthorEmployee().getId() : null)
            .position(entity.getPosition() != null ? entity.getPosition().getId() : null)
            .positionImportance(entity.getPositionImportance() != null ? entity.getPositionImportance().getId() : null)
            .positionImportanceName(entity.getPositionImportance() != null ? entity.getPositionImportance().getName() : null)
            .build();
    }

}
