package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionPositionKrLevelDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionKrLevelEntity;

@UtilityClass
public class PositionPositionKrLevelFactory {

    public static PositionPositionKrLevelDto create(PositionPositionKrLevelEntity entity) {
        return new PositionPositionKrLevelDto(entity.getId().getPosition().getId(), entity.getId().getPositionKrLevel().getId());
    }
}
