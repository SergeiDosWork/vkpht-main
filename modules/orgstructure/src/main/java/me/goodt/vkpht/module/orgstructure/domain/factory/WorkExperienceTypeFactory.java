package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.WorkExperienceTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkExperienceTypeEntity;

@UtilityClass
public class WorkExperienceTypeFactory {

    public static WorkExperienceTypeDto create(WorkExperienceTypeEntity entity) {
        return new WorkExperienceTypeDto(entity.getId(), entity.getName(), entity.getDescription(),
            entity.getDateFrom(), entity.getDateTo());
    }
}
