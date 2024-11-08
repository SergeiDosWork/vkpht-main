package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentCategoryDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentCategoryEntity;

@Component
public class AssignmentCategoryMapper implements CrudDtoMapper<AssignmentCategoryEntity, AssignmentCategoryDto> {

    @Override
    public AssignmentCategoryDto toDto(AssignmentCategoryEntity entity) {
        AssignmentCategoryDto dto = new AssignmentCategoryDto();
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
    public AssignmentCategoryEntity toNewEntity(AssignmentCategoryDto dto) {
        AssignmentCategoryEntity entity = new AssignmentCategoryEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public AssignmentCategoryEntity toUpdatedEntity(AssignmentCategoryDto dto, AssignmentCategoryEntity entity) {
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setFullName(dto.getFullName());
        entity.setShortName(dto.getShortName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }
}
