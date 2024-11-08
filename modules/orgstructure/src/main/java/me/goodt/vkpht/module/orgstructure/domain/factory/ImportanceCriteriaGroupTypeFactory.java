package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceCriteriaGroupTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupTypeEntity;

@UtilityClass
public class ImportanceCriteriaGroupTypeFactory {

    public static ImportanceCriteriaGroupTypeDto create(ImportanceCriteriaGroupTypeEntity entity) {
        return new ImportanceCriteriaGroupTypeDto(entity.getId(), entity.getName());
    }
}
