package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.FunctionDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.FunctionEntity;

@UtilityClass
public class FunctionFactory {

    public static FunctionDto create(FunctionEntity entity) {
        return new FunctionDto(entity.getId(), entity.getParent() != null ? entity.getParent().getId() : null,
            entity.getPrecursor() != null ? entity.getPrecursor().getId() : null,
            entity.getDateFrom(), entity.getDateTo(), entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getStatus() != null ? entity.getStatus().getId().longValue() : null);
    }
}
