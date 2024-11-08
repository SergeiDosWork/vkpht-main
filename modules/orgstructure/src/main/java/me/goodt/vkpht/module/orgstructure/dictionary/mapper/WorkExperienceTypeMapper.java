package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.WorkExperienceTypeDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkExperienceTypeEntity;

@Component
public class WorkExperienceTypeMapper implements CrudDtoMapper<WorkExperienceTypeEntity, WorkExperienceTypeDto> {

    @Override
    public WorkExperienceTypeEntity toNewEntity(WorkExperienceTypeDto dto) {
        WorkExperienceTypeEntity entity = new WorkExperienceTypeEntity();
        entity.setDateFrom(new Date());
        toUpdatedEntity(dto, entity);
        return entity;
    }

    @Override
    public WorkExperienceTypeEntity toUpdatedEntity(WorkExperienceTypeDto dto, WorkExperienceTypeEntity entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }

    @Override
    public WorkExperienceTypeDto toDto(WorkExperienceTypeEntity entity) {
        WorkExperienceTypeDto dto = new WorkExperienceTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }
}
