package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.CalculationMethodDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.CalculationMethodEntity;

@UtilityClass
public class CalculationMethodFactory {

    public static CalculationMethodDto create(CalculationMethodEntity entity) {
        return new CalculationMethodDto(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getDescription()
        );
    }
}
