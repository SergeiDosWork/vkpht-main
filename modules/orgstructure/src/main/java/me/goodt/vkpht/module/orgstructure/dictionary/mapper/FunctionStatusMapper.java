package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.FunctionStatusDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.FunctionStatusEntity;

@Component
public class FunctionStatusMapper implements CrudDtoMapper<FunctionStatusEntity, FunctionStatusDto> {
    @Override
    public FunctionStatusDto toDto(FunctionStatusEntity entity) {
        FunctionStatusDto dto = new FunctionStatusDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());
        return dto;
    }

    @Override
    public FunctionStatusEntity toNewEntity(FunctionStatusDto dto) {
        FunctionStatusEntity entity = new FunctionStatusEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public FunctionStatusEntity toUpdatedEntity(FunctionStatusDto dto, FunctionStatusEntity entity) {
        entity.setName(dto.getName());
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
        return entity;
    }


}
