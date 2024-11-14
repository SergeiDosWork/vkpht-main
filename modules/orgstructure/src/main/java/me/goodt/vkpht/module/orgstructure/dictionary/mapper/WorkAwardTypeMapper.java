package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.WorkAwardTypeDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkAwardTypeEntity;

@Component
public class WorkAwardTypeMapper implements CrudDtoMapper<WorkAwardTypeEntity, WorkAwardTypeDto> {

    @Override
    public WorkAwardTypeEntity toNewEntity(WorkAwardTypeDto dto) {
        WorkAwardTypeEntity entity = new WorkAwardTypeEntity();
        toUpdatedEntity(dto, entity);
        return entity;
    }

    @Override
    public WorkAwardTypeEntity toUpdatedEntity(WorkAwardTypeDto dto, WorkAwardTypeEntity entity) {
        entity.setName(dto.getName());
        return entity;
    }

    @Override
    public WorkAwardTypeDto toDto(WorkAwardTypeEntity entity) {
        WorkAwardTypeDto dto = new WorkAwardTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }
}
