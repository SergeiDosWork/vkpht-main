package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.EmployeeStatusDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeStatusEntity;

@Component
public class EmployeeStatusMapper implements CrudDtoMapper<EmployeeStatusEntity, EmployeeStatusDto> {

    @Override
    public EmployeeStatusDto toDto(EmployeeStatusEntity entity) {
        EmployeeStatusDto dto = new EmployeeStatusDto();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setIsFreeStake(entity.getIsFreeStake());
        dto.setShortName(entity.getShortName());
        dto.setSystemName(entity.getSystemName());
        dto.setExternalId(entity.getExternalId());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }

    @Override
    public EmployeeStatusEntity toNewEntity(EmployeeStatusDto dto) {
        EmployeeStatusEntity entity = new EmployeeStatusEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public EmployeeStatusEntity toUpdatedEntity(EmployeeStatusDto dto, EmployeeStatusEntity entity) {
        entity.setFullName(dto.getFullName());
        entity.setIsFreeStake(dto.getIsFreeStake());
        entity.setShortName(dto.getShortName());
        entity.setSystemName(dto.getSystemName());
        entity.setExternalId(dto.getExternalId());
        entity.setUpdateDate(new Date());
        return entity;
    }
}
