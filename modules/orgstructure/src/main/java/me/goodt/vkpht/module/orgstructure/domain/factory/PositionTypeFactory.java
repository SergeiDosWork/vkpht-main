package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionTypeEntity;

@UtilityClass
public class PositionTypeFactory {

    public static PositionTypeDto create(PositionTypeEntity entity) {
        return new PositionTypeDto(entity.getId(), entity.getName(), entity.getDescription(),
            entity.getDateFrom(), entity.getDateTo());
    }
}
