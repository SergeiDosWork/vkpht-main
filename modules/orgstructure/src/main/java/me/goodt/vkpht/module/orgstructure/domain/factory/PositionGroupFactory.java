package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionGroupDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupEntity;

@UtilityClass
public class PositionGroupFactory {

    public static PositionGroupDto create(PositionGroupEntity entity) {
        return new PositionGroupDto(entity.getId(), entity.getName(), entity.getDescription());
    }
}
