package me.goodt.vkpht.module.orgstructure.domain.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionGroupDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupEntity;

@Component
public class PositionGroupMapper implements CrudDtoMapper<PositionGroupEntity, PositionGroupDto> {

    @Override
    public PositionGroupDto toDto(PositionGroupEntity entity) {
        PositionGroupDto dto = new PositionGroupDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());

        return dto;
    }

    @Override
    public PositionGroupEntity toNewEntity(PositionGroupDto dto) {
        PositionGroupEntity entity = new PositionGroupEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDateFrom(new Date());

        return entity;
    }

    @Override
    public PositionGroupEntity toUpdatedEntity(PositionGroupDto dto, PositionGroupEntity entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
