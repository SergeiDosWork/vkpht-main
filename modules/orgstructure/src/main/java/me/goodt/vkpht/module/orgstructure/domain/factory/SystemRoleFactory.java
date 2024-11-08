package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.SystemRoleDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.SystemRoleEntity;

@UtilityClass
public class SystemRoleFactory {

    public static SystemRoleDto create(SystemRoleEntity entity) {
        return new SystemRoleDto(entity.getId(), entity.getName(), entity.getIsAssignable(), entity.getDateFrom(), entity.getDateTo());
    }
}
