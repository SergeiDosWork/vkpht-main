package me.goodt.vkpht.module.orgstructure.domain.factory;


import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceCriteriaDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaEntity;

@UtilityClass
public class ImportanceCriteriaFactory {

    public static ImportanceCriteriaDto create(ImportanceCriteriaEntity entity) {
        return new ImportanceCriteriaDto(
            entity.getId(),
            entity.getGroup() != null ? entity.getGroup().getId() : null,
            entity.getName(),
            entity.getDescription(),
            entity.getWeight(),
            entity.getIsEnabled(),
            entity.getCalculationMethod() != null ? CalculationMethodFactory.create(entity.getCalculationMethod()) : null
        );
    }
}
