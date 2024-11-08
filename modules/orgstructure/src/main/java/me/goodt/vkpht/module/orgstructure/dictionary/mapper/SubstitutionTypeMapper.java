package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.SubstitutionTypeDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.SubstitutionTypeEntity;

@Component
public class SubstitutionTypeMapper implements CrudDtoMapper<SubstitutionTypeEntity, SubstitutionTypeDto> {
    @Override
    public SubstitutionTypeDto toDto(SubstitutionTypeEntity entity) {
        SubstitutionTypeDto dto = new SubstitutionTypeDto();
        dto.setId(entity.getId());
        dto.setAbbreviation(entity.getAbbreviation());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setFullName(entity.getFullName());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setExternalId(entity.getExternalId());
        dto.setShortName(entity.getShortName());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }

    @Override
    public SubstitutionTypeEntity toNewEntity(SubstitutionTypeDto dto) {
        SubstitutionTypeEntity entity = new SubstitutionTypeEntity();
        entity.setDateFrom(new Date());
        toUpdatedEntity(dto, entity);
        return entity;
    }

    @Override
    public SubstitutionTypeEntity toUpdatedEntity(SubstitutionTypeDto dto, SubstitutionTypeEntity entity) {
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setFullName(dto.getFullName());
        entity.setExternalId(dto.getExternalId());
        entity.setShortName(dto.getShortName());
        entity.setUpdateDate(new Date());
        return entity;
    }
}
