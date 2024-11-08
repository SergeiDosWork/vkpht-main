package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonnelDocumentTypeDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentTypeEntity;

@Component
public class PersonnelDocumentTypeMapper implements CrudDtoMapper<PersonnelDocumentTypeEntity, PersonnelDocumentTypeDto> {

    @Override
    public PersonnelDocumentTypeDto toDto(PersonnelDocumentTypeEntity entity) {
        PersonnelDocumentTypeDto dto = new PersonnelDocumentTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setExternalId(entity.getExternalId());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }

    @Override
    public PersonnelDocumentTypeEntity toNewEntity(PersonnelDocumentTypeDto dto) {
        PersonnelDocumentTypeEntity entity = new PersonnelDocumentTypeEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public PersonnelDocumentTypeEntity toUpdatedEntity(PersonnelDocumentTypeDto dto, PersonnelDocumentTypeEntity entity) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }
}
