package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonnelDocumentFormDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentFormEntity;

@Component
public class PersonnelDocumentFormMapper implements CrudDtoMapper<PersonnelDocumentFormEntity, PersonnelDocumentFormDto> {

    @Override
    public PersonnelDocumentFormDto toDto(PersonnelDocumentFormEntity entity) {
        PersonnelDocumentFormDto dto = new PersonnelDocumentFormDto();
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
    public PersonnelDocumentFormEntity toNewEntity(PersonnelDocumentFormDto dto) {
        PersonnelDocumentFormEntity entity = new PersonnelDocumentFormEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public PersonnelDocumentFormEntity toUpdatedEntity(PersonnelDocumentFormDto dto, PersonnelDocumentFormEntity entity) {
        entity.setName(dto.getName());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }
}
