package me.goodt.vkpht.module.orgstructure.api;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentRotationEntity;

public interface DivisionTeamAssignmentRotationService {
    DivisionTeamAssignmentRotationEntity getDivisionTeamAssignmentRotation(Long id) throws NotFoundException;

    void updateDivisionTeamAssignmentRotationComment(Long id, String commentHr, String commentEmployee) throws NotFoundException;
}
