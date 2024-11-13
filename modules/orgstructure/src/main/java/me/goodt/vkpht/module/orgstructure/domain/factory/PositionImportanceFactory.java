package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionImportanceDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceEntity;

@UtilityClass
public class PositionImportanceFactory {

    public static PositionImportanceDto create(PositionImportanceEntity entity) {
        return new PositionImportanceDto(entity.getId(), entity.getName(), entity.getSuccessorCountMax(),
            entity.getSuccessorCountRec(), entity.getDescription(), entity.getIndex(),
            entity.getDateFrom(), entity.getDateTo());
    }
}
