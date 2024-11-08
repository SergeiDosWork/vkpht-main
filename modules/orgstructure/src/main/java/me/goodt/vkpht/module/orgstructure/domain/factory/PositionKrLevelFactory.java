package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionKrLevelDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionKrLevelEntity;

@UtilityClass
public class PositionKrLevelFactory {

    public static PositionKrLevelDto create(PositionKrLevelEntity entity) {
        return new PositionKrLevelDto(entity.getId(), entity.getName(), entity.getDescription());
    }
}
