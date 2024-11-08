package me.goodt.vkpht.module.orgstructure.domain.mapper;

import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionRankDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionRankEntity;

@Component
public class PositionRankMapper implements CrudDtoMapper<PositionRankEntity, PositionRankDto> {

    @Override
    public PositionRankDto toDto(PositionRankEntity entity) {
        PositionRankDto dto = new PositionRankDto();
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

    @Override
    public PositionRankEntity toNewEntity(PositionRankDto dto) {
        PositionRankEntity entity = new PositionRankEntity();
        entity.setDateFrom(new Date());
        toUpdatedEntity(dto, entity);

        return entity;
    }

    @Override
    public PositionRankEntity toUpdatedEntity(PositionRankDto dto, PositionRankEntity entity) {
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setFullName(dto.getFullName());
        entity.setShortName(dto.getShortName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());

        return entity;
    }
}
