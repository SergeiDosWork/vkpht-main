package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.RoleDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;

@UtilityClass
public class RoleFactory {

    public static RoleDto create(RoleEntity entity) {
        return new RoleDto(
                entity.getId(),
                entity.getFullName(),
                entity.getShortName(),
                entity.getAbbreviation(),
                entity.getSystemRole() != null ? SystemRoleFactory.create(entity.getSystemRole()) : null,
                entity.getCode(),
                entity.getDateFrom(),
                entity.getDateTo()
        );
    }
}
