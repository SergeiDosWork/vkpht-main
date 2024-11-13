
package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionImportanceCriteriaDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceCriteriaEntity;

@UtilityClass
public class PositionImportanceCriteriaFactory {

    public static PositionImportanceCriteriaDto create(PositionImportanceCriteriaEntity entity) {
        return new PositionImportanceCriteriaDto(
            entity.getId(),
            entity.getDateFrom(),
            entity.getDateTo(),
            entity.getPosition().getId(),
            entity.getImportanceCriteria().getId(),
            entity.getWeight(),
            entity.getValue()
        );
    }
}
