package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.common.api.dto.RoleInfo;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentEntity;

public interface RoleService {
    List<RoleInfo> getActualAssignableRoleList();

    List<LegalEntityTeamAssignmentDto> getLegalTeamAssignmentInfo(List<Long> legalEntityIds, List<Long> roleIds, List<Long> employeeIds);

    LegalEntityTeamAssignmentEntity setEmployeeRole(Long legalEntityId, Long roleId, Long employeeId) throws NotFoundException;

    LegalEntityTeamAssignmentEntity clearEmployeeRole(Long legalEntityId, Long roleId, Long employeeId);

    List<LegalEntityTeamAssignmentDto> getHrLegalEntityTeamAssignment(Long assignmentId, Long employeeId);
}
