package me.goodt.vkpht.module.orgstructure.api;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleRawDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleEntity;

public interface DivisionTeamRoleService {
    DivisionTeamRoleEntity create(DivisionTeamRoleRawDto dto) throws NotFoundException;

    DivisionTeamRoleEntity update(DivisionTeamRoleRawDto dto) throws NotFoundException;
}
