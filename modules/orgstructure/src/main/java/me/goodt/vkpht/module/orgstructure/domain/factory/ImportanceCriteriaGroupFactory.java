package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceCriteriaGroupDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupEntity;

@UtilityClass
public class ImportanceCriteriaGroupFactory {

    public static ImportanceCriteriaGroupDto create(ImportanceCriteriaGroupEntity entity) {
        return new ImportanceCriteriaGroupDto(entity.getId(), entity.getTypeId().getId(), entity.getName(), entity.getDescription(), entity.getIsEditable());
    }
}
