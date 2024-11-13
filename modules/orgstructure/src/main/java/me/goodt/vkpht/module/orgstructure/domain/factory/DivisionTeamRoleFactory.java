package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.*;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleRawDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleShortDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamRoleShort;

@UtilityClass
public class DivisionTeamRoleFactory {

    public static DivisionTeamRoleDto create(DivisionTeamRoleEntity entity) {
        return new DivisionTeamRoleDto(entity.getId(),
            entity.getDivisionTeam() != null ? DivisionTeamFactory.create(entity.getDivisionTeam()) : null,
            entity.getRole() != null ? RoleFactory.create(entity.getRole()) : null,
            entity.getPositionImportance() != null ? PositionImportanceFactory.create(entity.getPositionImportance()) : null,
            entity.getExternalId());
    }

    public static DivisionTeamRoleRawDto createRaw(DivisionTeamRoleEntity entity) {
        return new DivisionTeamRoleRawDto(entity.getId(),
            entity.getDivisionTeam() != null ? entity.getDivisionTeam().getId() : null,
            entity.getRole() != null ? entity.getRole().getId() : null,
            entity.getPositionImportance() != null ? entity.getPositionImportance().getId() : null,
            entity.getExternalId());
    }

    public static DivisionTeamRoleShortDto createShort(DivisionTeamRoleShort sh) {
        return new DivisionTeamRoleShortDto(sh.getId(), sh.getDivisionTeamId(), sh.getRoleId(), sh.getExternalId());
    }
}
