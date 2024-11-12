package me.goodt.vkpht.module.orgstructure.api;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleContainerDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleRawDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleEntity;

import java.util.Date;
import java.util.List;

public interface DivisionTeamRoleService {
    DivisionTeamRoleEntity create(DivisionTeamRoleRawDto dto) throws NotFoundException;

    DivisionTeamRoleEntity update(DivisionTeamRoleRawDto dto) throws NotFoundException;

    List<DivisionTeamRoleContainerDto> findDivisionTeamRoles(Integer page,
                                                             Integer size,
                                                             Boolean successorReadinessDateFromPlusYear,
                                                             Date successorReadinessDateFromStart,
                                                             Date successorReadinessDateFromEnd,
                                                             Date assignmentRotationDateFromStart,
                                                             Date assignmentRotationDateFromEnd,
                                                             Date successorDateFrom,
                                                             List<Long> divisionTeamRoleIds,
                                                             List<Long> divisionTeamIds,
                                                             Long divisionId,
                                                             String searchingValue,
                                                             Long employeeSuccessorId,
                                                             List<Long> legalEntityIds,
                                                             boolean isDivisionTeamRoleIdOnlyParam);
}
