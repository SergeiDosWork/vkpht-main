package me.goodt.vkpht.module.orgstructure.dictionary.mapper;


import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentRotationDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentRotationEntity;

@Component
public class AssignmentRotationMapper implements CrudDtoMapper<AssignmentRotationEntity, AssignmentRotationDto> {
    @Override
    public AssignmentRotationEntity toNewEntity(AssignmentRotationDto dto) {
        AssignmentRotationEntity entity = new AssignmentRotationEntity();
        entity = toUpdatedEntity(dto, entity);
        entity.setDateFrom(new Date());
        return entity;
    }

    @Override
    public AssignmentRotationEntity toUpdatedEntity(AssignmentRotationDto dto, AssignmentRotationEntity entity) {
        entity.setName(dto.getName());
        return entity;
    }

    @Override
    public AssignmentRotationDto toDto(AssignmentRotationEntity entity) {
        AssignmentRotationDto dto = new AssignmentRotationDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());

        return dto;
    }
}
