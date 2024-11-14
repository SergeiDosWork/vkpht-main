package me.goodt.vkpht.module.orgstructure.domain.mapper;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionGradeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGradeEntity;

@Component
public class PositionGradeMapper implements CrudDtoMapper<PositionGradeEntity, PositionGradeDto> {

    @Override
    public PositionGradeDto toDto(PositionGradeEntity entity) {
        PositionGradeDto dto = new PositionGradeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setIndex(entity.getIndex());

        return dto;
    }

    @Override
    public PositionGradeEntity toNewEntity(PositionGradeDto dto) {
        PositionGradeEntity entity = new PositionGradeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIndex(dto.getIndex());

        return entity;
    }

    @Override
    public PositionGradeEntity toUpdatedEntity(PositionGradeDto dto, PositionGradeEntity entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIndex(dto.getIndex());

        return entity;
    }
}
