package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PrivilegeDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PrivilegeEntity;

@Component
public class PrivilegeMapper implements CrudDtoMapper<PrivilegeEntity, PrivilegeDto> {

    @Override
    public PrivilegeEntity toNewEntity(PrivilegeDto dto) {
        PrivilegeEntity entity = new PrivilegeEntity();
        entity.setDateFrom(new Date());
        toUpdatedEntity(dto, entity);
        return entity;
    }

    @Override
    public PrivilegeEntity toUpdatedEntity(PrivilegeDto dto, PrivilegeEntity entity) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }

    @Override
    public PrivilegeDto toDto(PrivilegeEntity entity) {
        PrivilegeDto dto = new PrivilegeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }
}
