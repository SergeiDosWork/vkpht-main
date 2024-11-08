package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionRankDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionRankEntity;

@UtilityClass
public class PositionRankFactory {

    public static PositionRankDto create(PositionRankEntity entity) {
        return new PositionRankDto(entity.getId(), entity.getFullName(), entity.getShortName(),
                entity.getAbbreviation(), entity.getDateFrom(), entity.getDateTo());
    }
}
