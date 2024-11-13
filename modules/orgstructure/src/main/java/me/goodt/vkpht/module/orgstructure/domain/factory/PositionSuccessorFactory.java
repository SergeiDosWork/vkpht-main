package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorEntity;

@UtilityClass
public class PositionSuccessorFactory {

    public static PositionSuccessorDto create(PositionSuccessorEntity entity) {
        return new PositionSuccessorDto(
            entity.getId(),
            entity.getDateCommitHr(),
            entity.getDatePriority(),
            entity.getEmployee() == null ? null : EmployeeInfoFactory.create(entity.getEmployee()),
            entity.getPosition() == null ? null : PositionFactory.create(entity.getPosition()),
            entity.getPositionGroup() == null ? null : PositionGroupFactory.create(entity.getPositionGroup()),
            entity.getReasonInclusion() == null ? null : ReasonFactory.create(entity.getReasonInclusion()),
            entity.getReasonExclusion() == null ? null : ReasonFactory.create(entity.getReasonExclusion()),
            entity.getCommentInclusion(),
            entity.getCommentExclusion(),
            entity.getDocumentUrlInclusion(),
            entity.getDocumentUrlExclusion(),
            entity.getDateFrom(),
            entity.getDateTo()
        );
    }
}
