package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.WorkWeekTypeDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkWeekTypeEntity;

@Component
public class WorkWeekTypeMapper implements CrudDtoMapper<WorkWeekTypeEntity, WorkWeekTypeDto> {
    @Override
    public WorkWeekTypeDto toDto(WorkWeekTypeEntity entity) {
        WorkWeekTypeDto dto = new WorkWeekTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    @Override
    public WorkWeekTypeEntity toNewEntity(WorkWeekTypeDto dto) {
        WorkWeekTypeEntity entity = new WorkWeekTypeEntity();
        toUpdatedEntity(dto, entity);
        return entity;
    }

    @Override
    public WorkWeekTypeEntity toUpdatedEntity(WorkWeekTypeDto dto, WorkWeekTypeEntity entity) {
        entity.setName(dto.getName());
        return entity;
    }
}
