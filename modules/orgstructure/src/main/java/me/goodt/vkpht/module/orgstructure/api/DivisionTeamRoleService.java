package me.goodt.vkpht.module.orgstructure.api;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleContainerDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleRawDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.dto.request.FindDivisionTeamRolesRequest;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleEntity;

import java.util.Date;
import java.util.List;

public interface DivisionTeamRoleService {
    DivisionTeamRoleEntity create(DivisionTeamRoleRawDto dto) throws NotFoundException;

    DivisionTeamRoleEntity update(DivisionTeamRoleRawDto dto) throws NotFoundException;

    List<DivisionTeamRoleContainerDto> findDivisionTeamRoles(FindDivisionTeamRolesRequest request);
}
