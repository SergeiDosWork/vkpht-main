package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionKrLevelDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionKrLevelEntity;

@Component
public class PositionKrLevelMapper implements CrudDtoMapper<PositionKrLevelEntity, PositionKrLevelDto> {
    @Override
    public PositionKrLevelEntity toNewEntity(PositionKrLevelDto dto) {
        PositionKrLevelEntity entity = new PositionKrLevelEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public PositionKrLevelEntity toUpdatedEntity(PositionKrLevelDto dto, PositionKrLevelEntity entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public PositionKrLevelDto toDto(PositionKrLevelEntity entity) {
        PositionKrLevelDto dto = new PositionKrLevelDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
