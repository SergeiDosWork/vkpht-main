package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.StructureStatusDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.StructureStatusEntity;

@Component
public class StructureStatusMapper implements CrudDtoMapper<StructureStatusEntity, StructureStatusDto> {
    @Override
    public StructureStatusEntity toNewEntity(StructureStatusDto dto) {
        StructureStatusEntity entity = new StructureStatusEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public StructureStatusEntity toUpdatedEntity(StructureStatusDto dto, StructureStatusEntity entity) {
        entity.setName(dto.getName());
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
        return entity;
    }

    @Override
    public StructureStatusDto toDto(StructureStatusEntity entity) {
        StructureStatusDto dto = new StructureStatusDto();
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
