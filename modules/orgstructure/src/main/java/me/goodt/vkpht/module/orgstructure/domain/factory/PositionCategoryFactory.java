package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionCategoryDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionCategoryEntity;

@UtilityClass
public class PositionCategoryFactory {

    public static PositionCategoryDto create(PositionCategoryEntity entity) {
        return new PositionCategoryDto(entity.getId(), entity.getFullName(),
                entity.getShortName(), entity.getAbbreviation(),
                entity.getDateFrom(), entity.getDateTo());
    }
}
