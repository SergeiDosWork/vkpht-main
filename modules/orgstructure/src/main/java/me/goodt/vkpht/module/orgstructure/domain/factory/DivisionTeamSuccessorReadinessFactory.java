package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorReadinessDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamSuccessorReadinessEntity;

@UtilityClass
public class DivisionTeamSuccessorReadinessFactory {

    public static DivisionTeamSuccessorReadinessDto create(DivisionTeamSuccessorReadinessEntity entity) {
        return new DivisionTeamSuccessorReadinessDto(
            entity.getId(),
            entity.getDateFrom(),
            entity.getDateTo(),
            entity.getDivisionTeamSuccessor() != null ? DivisionTeamSuccessorFactory.create(entity.getDivisionTeamSuccessor()) : null,
            entity.getReadiness() != null ? AssignmentReadinessFactory.create(entity.getReadiness()) : null
        );
    }
}
