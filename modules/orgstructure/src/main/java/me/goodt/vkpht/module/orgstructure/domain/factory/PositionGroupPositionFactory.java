package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionGroupPositionDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupPositionEntity;

@UtilityClass
public class PositionGroupPositionFactory {

    public static PositionGroupPositionDto create(PositionGroupPositionEntity entity) {
        return new PositionGroupPositionDto(
            entity.getId(),
            entity.getPosition().getId(),
            entity.getPositionGroup().getId(),
            entity.getDateFrom(),
            entity.getDateTo());
    }
}
