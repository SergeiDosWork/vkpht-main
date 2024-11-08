package me.goodt.vkpht.module.orgstructure.domain.mapper;

import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import com.goodt.drive.rtcore.dictionary.orgstructure.dto.PositionTypeDto;
import com.goodt.drive.rtcore.model.orgstructure.entities.PositionTypeEntity;

@Component
public class PositionTypeMapper implements CrudDtoMapper<PositionTypeEntity, PositionTypeDto> {

    @Override
    public PositionTypeDto toDto(PositionTypeEntity entity) {
        PositionTypeDto dto = new PositionTypeDto();
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

    @Override
    public PositionTypeEntity toNewEntity(PositionTypeDto dto) {
        PositionTypeEntity entity = new PositionTypeEntity();
        entity.setDateFrom(new Date());
        toUpdatedEntity(dto, entity);

        return entity;
    }

    @Override
    public PositionTypeEntity toUpdatedEntity(PositionTypeDto dto, PositionTypeEntity entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());

        return entity;
    }
}
