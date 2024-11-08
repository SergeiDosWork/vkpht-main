package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.AssignmentStatusDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentStatusEntity;

@UtilityClass
public class AssignmentStatusFactory {

    public static AssignmentStatusDto create(AssignmentStatusEntity entity){
        return new AssignmentStatusDto(entity.getId(), entity.getFullName(), entity.getShortName(),
                entity.getAbbreviation(), entity.getDateFrom(), entity.getDateTo());
    }
}
