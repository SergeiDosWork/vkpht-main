package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.WorkFunctionStatusDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkFunctionStatusEntity;

@Component
public class WorkFunctionStatusMapper implements CrudDtoMapper<WorkFunctionStatusEntity, WorkFunctionStatusDto> {
    @Override
    public WorkFunctionStatusDto toDto(WorkFunctionStatusEntity entity) {
        WorkFunctionStatusDto dto = new WorkFunctionStatusDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateTo(entity.getDateTo());
        dto.setDateFrom(entity.getDateFrom());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());
        dto.setExternalId(entity.getExternalId());
        return dto;
    }

    @Override
    public WorkFunctionStatusEntity toNewEntity(WorkFunctionStatusDto dto) {
        WorkFunctionStatusEntity entity = new WorkFunctionStatusEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public WorkFunctionStatusEntity toUpdatedEntity(WorkFunctionStatusDto dto, WorkFunctionStatusEntity entity) {
        entity.setName(dto.getName());
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
        return entity;
    }
}
