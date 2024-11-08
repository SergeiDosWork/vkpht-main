package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentRotationDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentRotationShortDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentRotationEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

/**
 * @author Pavel Khovaylo
 */
@UtilityClass
public class DivisionTeamAssignmentRotationFactory {

    public static DivisionTeamAssignmentRotationDto create(DivisionTeamAssignmentRotationEntity entity) {
        return new DivisionTeamAssignmentRotationDto(entity.getId(), entity.getDateFrom(), entity.getDateTo(), entity.getDateCommitHr(),
                                                     entity.getRotation() != null ? AssignmentRotationFactory.create(entity.getRotation()) : null,
                                                     entity.getCommentHr(), entity.getCommentEmployee(),
                                                     entity.getAssignment() != null ? DivisionTeamAssignmentFactory.createShort(entity.getAssignment(), null) : null);
    }

    public static DivisionTeamAssignmentRotationDto createWithJobInfo(DivisionTeamAssignmentRotationEntity entity,
                                                                      List<PositionAssignmentEntity> positionAssignments) {
        return new DivisionTeamAssignmentRotationDto(entity.getId(), entity.getDateFrom(), entity.getDateTo(), entity.getDateCommitHr(),
                                                     entity.getRotation() != null ? AssignmentRotationFactory.create(entity.getRotation()) : null,
                                                     entity.getCommentHr(), entity.getCommentEmployee(),
                                                     entity.getAssignment() != null ? DivisionTeamAssignmentFactory.createShortWithJobInfo(entity.getAssignment(), positionAssignments, null) : null);
    }

    public static DivisionTeamAssignmentRotationShortDto createShort(DivisionTeamAssignmentRotationEntity entity) {
        return new DivisionTeamAssignmentRotationShortDto(entity.getId(), entity.getDateFrom(), entity.getDateTo(), entity.getDateCommitHr(),
                                                          entity.getRotation() != null ? AssignmentRotationFactory.create(entity.getRotation()) : null,
                                                          entity.getCommentHr(), entity.getCommentEmployee());
    }
}
