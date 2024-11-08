package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.StructureTypeDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.StructureTypeEntity;

@Component
public class StructureTypeMapper implements CrudDtoMapper<StructureTypeEntity, StructureTypeDto> {
    @Override
    public StructureTypeEntity toNewEntity(StructureTypeDto dto) {
        StructureTypeEntity entity = new StructureTypeEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public StructureTypeEntity toUpdatedEntity(StructureTypeDto dto, StructureTypeEntity entity) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }

    @Override
    public StructureTypeDto toDto(StructureTypeEntity entity) {
        StructureTypeDto dto = new StructureTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setExternalId(entity.getExternalId());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }
}
