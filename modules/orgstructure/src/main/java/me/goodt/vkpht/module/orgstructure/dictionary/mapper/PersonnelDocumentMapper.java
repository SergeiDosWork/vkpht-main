package me.goodt.vkpht.module.orgstructure.dictionary.mapper;


import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonnelDocumentDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentEntity;

@Component
public class PersonnelDocumentMapper implements CrudDtoMapper<PersonnelDocumentEntity, PersonnelDocumentDto> {
    @Override
    public PersonnelDocumentEntity toNewEntity(PersonnelDocumentDto dto) {
        PersonnelDocumentEntity entity = new PersonnelDocumentEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public PersonnelDocumentEntity toUpdatedEntity(PersonnelDocumentDto dto, PersonnelDocumentEntity entity) {
        entity.setName(dto.getName());
        entity.setData(dto.getData());
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }

    @Override
    public PersonnelDocumentDto toDto(PersonnelDocumentEntity entity) {
        PersonnelDocumentDto dto = new PersonnelDocumentDto();
        dto.setId(entity.getId());
        dto.setData(entity.getData());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setName(entity.getName());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());
        dto.setUpdateDate(entity.getUpdateDate());
        if (entity.getPrecursor() != null) {
            dto.setPrecursorId(entity.getPrecursor().getId());
        }
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getId());
        }
        if (entity.getForm() != null) {
            dto.setFormId(entity.getForm().getId());
        }
        if (entity.getType() != null) {
            dto.setTypeId(entity.getType().getId());
        }
        return dto;
    }
}
