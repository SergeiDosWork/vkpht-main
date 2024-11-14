package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionStatusDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionStatusEntity;

@Component
public class PositionStatusMapper implements CrudDtoMapper<PositionStatusEntity, PositionStatusDto> {

    @Override
    public PositionStatusEntity toNewEntity(PositionStatusDto dto) {
        PositionStatusEntity entity = new PositionStatusEntity();
        entity.setDateFrom(new Date());
        toUpdatedEntity(dto, entity);
        return entity;
    }

    @Override
    public PositionStatusEntity toUpdatedEntity(PositionStatusDto dto, PositionStatusEntity entity) {
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setFullName(dto.getFullName());
        entity.setShortName(dto.getShortName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }

    @Override
    public PositionStatusDto toDto(PositionStatusEntity entity) {
        PositionStatusDto dto = new PositionStatusDto();
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
