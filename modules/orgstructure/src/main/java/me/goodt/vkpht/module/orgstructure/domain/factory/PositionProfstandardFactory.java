package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionProfstandardDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionProfstandardEntity;

@UtilityClass
public class PositionProfstandardFactory {
    public static PositionProfstandardDto create(PositionProfstandardEntity entity) {
        return new PositionProfstandardDto(entity.getId(), entity.getCode(), entity.getDateFrom(),
                                           entity.getDateTo(), entity.getPosition().getId());
    }
}
