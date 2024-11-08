package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentEntity;

public interface ILegalEntityTeamAssignmentService {

    List<LegalEntityTeamAssignmentEntity> getLegalEntityTeamAssignments(Long employeeId);

    List<LegalEntityTeamAssignmentDto> getLegalEntityTeamAssignments(Long legalEntityTeamAssignmentId, Long employeeId, String externalEmployeeId, Long legalEntityTeamId);

    List<LegalEntityTeamAssignmentDto> getParentsLegalEntityTeamAssignments(Long employeeId, Long taskOwnerId, Long userTypeId);
}
