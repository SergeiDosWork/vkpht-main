package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentStatusDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentStatusEntity;

@Component
public class AssignmentStatusMapper implements CrudDtoMapper<AssignmentStatusEntity, AssignmentStatusDto> {


    @Override
    public AssignmentStatusEntity toNewEntity(AssignmentStatusDto dto) {
        AssignmentStatusEntity entity = new AssignmentStatusEntity();
        entity.setDateFrom(new Date());
        toUpdatedEntity(dto, entity);
        return entity;
    }

    @Override
    public AssignmentStatusEntity toUpdatedEntity(AssignmentStatusDto dto, AssignmentStatusEntity entity) {
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setFullName(dto.getFullName());
        entity.setShortName(dto.getShortName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }

    @Override
    public AssignmentStatusDto toDto(AssignmentStatusEntity entity) {
        AssignmentStatusDto dto = new AssignmentStatusDto();
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
}
