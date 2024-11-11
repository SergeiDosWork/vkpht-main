package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.domain.dao.SystemRoleDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.RoleDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class RoleAsm extends AbstractAsm<RoleEntity, RoleDto> {

    @Autowired
    private SystemRoleDao systemRoleDao;

    @Override
    public RoleDto toRes(RoleEntity entity) {
        RoleDto dto = new RoleDto();
        dto.setId(entity.getId());
        dto.setAbbreviation(entity.getAbbreviation());
        dto.setFullName(entity.getFullName());
        dto.setShortName(entity.getShortName());
        dto.setSystemRoleId(entity.getSystemRoleId());
        dto.setCode(entity.getCode());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());
        dto.setExternalId(entity.getExternalId());

        return dto;
    }

    @Override
    public void create(RoleEntity entity, RoleDto dto) {
        entity.setDateFrom(new Date());
        update(entity, dto);
    }

    @Override
    public void update(RoleEntity entity, RoleDto dto) {
        entity.setUpdateDate(new Date());
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setFullName(dto.getFullName());
        entity.setShortName(dto.getShortName());
        if (dto.getSystemRoleId() != null) {
            entity.setSystemRole(systemRoleDao.findById(dto.getSystemRoleId()).orElseThrow(() ->
                    new NotFoundException(String.format("SystemRole id = %d not found", dto.getSystemRoleId()))));
        } else {
            entity.setSystemRole(null);
        }
        entity.setCode(dto.getCode());
        entity.setExternalId(dto.getExternalId());
    }
}
