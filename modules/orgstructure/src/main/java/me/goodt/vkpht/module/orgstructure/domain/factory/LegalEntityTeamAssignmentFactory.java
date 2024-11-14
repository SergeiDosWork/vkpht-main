package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentEntity;

@UtilityClass
public class LegalEntityTeamAssignmentFactory {

    public static LegalEntityTeamAssignmentDto create(LegalEntityTeamAssignmentEntity legalEntityTeamAssignmentEntity) {
        return new LegalEntityTeamAssignmentDto(
            legalEntityTeamAssignmentEntity.getId(),
            legalEntityTeamAssignmentEntity.getPrecursorId(),
            legalEntityTeamAssignmentEntity.getDateFrom(), legalEntityTeamAssignmentEntity.getDateTo(),
            legalEntityTeamAssignmentEntity.getType() == null ? null : legalEntityTeamAssignmentEntity.getType().getId(),
            legalEntityTeamAssignmentEntity.getEmployee() == null ? null : EmployeePersonFactory.create(legalEntityTeamAssignmentEntity.getEmployee()),
            legalEntityTeamAssignmentEntity.getLegalEntityTeamEntity() == null ? null : LegalEntityTeamFactory.create(legalEntityTeamAssignmentEntity.getLegalEntityTeamEntity()),
            legalEntityTeamAssignmentEntity.getRole() == null ? null : RoleFactory.create(legalEntityTeamAssignmentEntity.getRole()),
            legalEntityTeamAssignmentEntity.getFullName(), legalEntityTeamAssignmentEntity.getShortName(), legalEntityTeamAssignmentEntity.getAbbreviation(),
            legalEntityTeamAssignmentEntity.getStatus() == null ? null : AssignmentStatusFactory.create(legalEntityTeamAssignmentEntity.getStatus())
        );
    }
}
