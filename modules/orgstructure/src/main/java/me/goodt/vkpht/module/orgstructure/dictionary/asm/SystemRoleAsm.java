package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.SystemRoleDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.SystemRoleEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;

@Component
public class SystemRoleAsm extends AbstractAsm<SystemRoleEntity, SystemRoleDto> {

    @Override
    public SystemRoleDto toRes(SystemRoleEntity entity) {
        SystemRoleDto dto = new SystemRoleDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIsAssignable(entity.getAssignable());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }

    @Override
    public void create(SystemRoleEntity entity, SystemRoleDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(SystemRoleEntity entity, SystemRoleDto dto) {
        entity.setName(dto.getName());
        entity.setAssignable(dto.getIsAssignable());
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
    }
}
