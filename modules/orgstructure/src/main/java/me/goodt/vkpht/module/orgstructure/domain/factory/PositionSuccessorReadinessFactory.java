package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorReadinessDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorReadinessEntity;

@UtilityClass
public class PositionSuccessorReadinessFactory {

    public static PositionSuccessorReadinessDto create(PositionSuccessorReadinessEntity entity) {
        return new PositionSuccessorReadinessDto(
            entity.getId(),
            entity.getPositionSuccessor() == null ? null : PositionSuccessorFactory.create(entity.getPositionSuccessor()),
            entity.getReadiness() == null ? null : AssignmentReadinessFactory.create(entity.getReadiness()),
            entity.getDateFrom(),
            entity.getDateTo()
        );
    }
}
