package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionImportanceDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceEntity;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PositionImportanceMapper implements CrudDtoMapper<PositionImportanceEntity, PositionImportanceDto> {
    @Override
    public PositionImportanceEntity toNewEntity(PositionImportanceDto dto) {
        PositionImportanceEntity entity = new PositionImportanceEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public PositionImportanceEntity toUpdatedEntity(PositionImportanceDto dto, PositionImportanceEntity entity) {
        entity.setName(dto.getName());
        entity.setSuccessorCountMax(dto.getSuccessorCountMax());
        entity.setSuccessorCountRec(dto.getSuccessorCountRec());
        entity.setDescription(dto.getDescription());
        entity.setIndex(dto.getIndex());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }

    @Override
    public PositionImportanceDto toDto(PositionImportanceEntity entity) {
        PositionImportanceDto dto = new PositionImportanceDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSuccessorCountMax(entity.getSuccessorCountMax());
        dto.setSuccessorCountRec(entity.getSuccessorCountRec());
        dto.setDescription(entity.getDescription());
        dto.setIndex(entity.getIndex());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }
}
