package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.ActualEducationDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.EducationTypeEntity;

@UtilityClass
public class EducationTypeFactory {

    public static ActualEducationDto create(EducationTypeEntity entity) {
        return new ActualEducationDto(entity.getId(),
            entity.getName() != null ? entity.getName() : null,
            entity.getExternalId() != null ? entity.getExternalId() : null,
            entity.getDateFrom(), entity.getDateTo());
    }
}
