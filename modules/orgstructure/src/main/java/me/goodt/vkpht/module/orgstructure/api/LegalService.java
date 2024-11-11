package me.goodt.vkpht.module.orgstructure.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityDto;
import me.goodt.vkpht.module.orgstructure.api.dto.projection.LegalEntityTeamAssignmentCompactProjection;

public interface LegalService {
    List<LegalEntityDto> getLegalEntityList(List<Long> divisionIds, List<Long> divisionGroupIds);

    LegalEntityDto getLegalEntityDto(Long id);

    Map<Long, List<Long>> getLegalEntityTeamIdList(Collection<Long> divisionTeamAssignmentIds);

    List<LegalEntityTeamAssignmentCompactProjection> getLegalEntityTeamAssignmentCompactByEmployeeId(Long employeeId);
}
