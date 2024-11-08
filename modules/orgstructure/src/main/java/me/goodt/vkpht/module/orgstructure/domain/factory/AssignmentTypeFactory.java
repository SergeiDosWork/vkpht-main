package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.AssignmentTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentTypeEntity;

@UtilityClass
public class AssignmentTypeFactory {

    public static AssignmentTypeDto create(AssignmentTypeEntity entity) {
        return new AssignmentTypeDto(entity.getId(), entity.getFullName(), entity.getShortName(),
                entity.getAbbreviation(), entity.getDateFrom(), entity.getDateTo());
    }
}
