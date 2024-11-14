package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.AssignmentRotationDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentRotationEntity;

/**
 * @author Pavel Khovaylo
 */
@UtilityClass
public class AssignmentRotationFactory {

    public static AssignmentRotationDto create(AssignmentRotationEntity entity) {
        return new AssignmentRotationDto(entity.getId(), entity.getName(), entity.getDateFrom(), entity.getDateTo());
    }
}
