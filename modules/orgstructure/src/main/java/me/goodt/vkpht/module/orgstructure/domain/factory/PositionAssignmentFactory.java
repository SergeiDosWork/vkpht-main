package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentExtendedDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

@UtilityClass
public class PositionAssignmentFactory {

    public static PositionAssignmentDto create(PositionAssignmentEntity assignment) {
        return new PositionAssignmentDto(assignment.getId(),
            assignment.getTypeId(),
            assignment.getPrecursorId(),
            assignment.getDateFrom(), assignment.getDateTo(),
            assignment.getEmployeeId(),
            assignment.getPositionId(),
            assignment.getStatus() == null ? null : assignment.getStatus().getId(),
            assignment.getPlacement() == null ? null : assignment.getPlacement().getId(),
            assignment.getSubstitutionType() == null ? null : assignment.getSubstitutionType().getId(),
            assignment.getFullName(), assignment.getShortName(), assignment.getAbbreviation(),
            assignment.getCategory() == null ? null : assignment.getCategory().getId(),
            assignment.getStake(), assignment.getProbationDateTo(),
            assignment.getPosition() == null ? null : PositionFactory.create(assignment.getPosition()),
            assignment.getType() == null ? null : AssignmentTypeFactory.create(assignment.getType()),
            assignment.getExternalId());
    }

    public static PositionAssignmentExtendedDto createExtended(PositionAssignmentEntity assignment) {
        return new PositionAssignmentExtendedDto(assignment.getId(),
            assignment.getTypeId(),
            assignment.getPrecursorId(),
            assignment.getDateFrom(), assignment.getDateTo(),
            assignment.getEmployee() == null ? null : EmployeeFactory.createExtended(assignment.getEmployee()),
            assignment.getPositionId(),
            assignment.getStatus() == null ? null : assignment.getStatus().getId(),
            assignment.getPlacement() == null ? null : assignment.getPlacement().getId(),
            assignment.getSubstitutionType() == null ? null : assignment.getSubstitutionType().getId(),
            assignment.getFullName(), assignment.getShortName(), assignment.getAbbreviation(),
            assignment.getCategory() == null ? null : assignment.getCategory().getId(),
            assignment.getStake(), assignment.getProbationDateTo(),
            assignment.getPosition() == null ? null : PositionFactory.create(assignment.getPosition()),
            assignment.getType() == null ? null : AssignmentTypeFactory.create(assignment.getType()),
            assignment.getExternalId());
    }

}
