package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionGroupDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionGroupEntity;

@UtilityClass
public class DivisionGroupFactory {

    public static DivisionGroupDto create(DivisionGroupEntity entity) {
        return new DivisionGroupDto(entity.getId(), entity.getName(), entity.getDateFrom(), entity.getDateTo());
    }
}
