package me.goodt.vkpht.module.orgstructure.domain.mapper;

import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionCategoryDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionCategoryEntity;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PositionCategoryMapper implements CrudDtoMapper<PositionCategoryEntity, PositionCategoryDto> {

    @Override
    public PositionCategoryDto toDto(PositionCategoryEntity entity) {
        PositionCategoryDto dto = new PositionCategoryDto();
        dto.setId(entity.getId());
        dto.setAbbreviation(entity.getAbbreviation());
        dto.setFullName(entity.getFullName());
        dto.setShortName(entity.getShortName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }

    @Override
    public PositionCategoryEntity toNewEntity(PositionCategoryDto dto) {
        PositionCategoryEntity entity = new PositionCategoryEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public PositionCategoryEntity toUpdatedEntity(PositionCategoryDto dto, PositionCategoryEntity entity) {
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setFullName(dto.getFullName());
        entity.setShortName(dto.getShortName());
        return entity;
    }
}
