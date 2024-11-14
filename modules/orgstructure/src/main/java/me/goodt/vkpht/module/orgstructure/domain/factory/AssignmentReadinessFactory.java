package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.AssignmentReadinessDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentReadinessEntity;

@UtilityClass
public class AssignmentReadinessFactory {

    public static AssignmentReadinessDto create(AssignmentReadinessEntity entity) {
        return new AssignmentReadinessDto(entity.getId(), entity.getName(), entity.getDateFrom(), entity.getDateTo());
    }
}
