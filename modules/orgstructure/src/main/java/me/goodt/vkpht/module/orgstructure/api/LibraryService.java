package me.goodt.vkpht.module.orgstructure.api;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentRotationEntity;

/**
 * @author Pavel Khovaylo
 */
public interface LibraryService {
    AssignmentRotationEntity getAssignmentRotation(Integer id) throws NotFoundException;

    AssignmentReadinessEntity getAssignmentReadiness(Integer id) throws NotFoundException;
}
