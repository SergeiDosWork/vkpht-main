package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonPrivilegeDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonPrivilegeEntity;

@Component
public class PersonPrivilegeMapper implements CrudDtoMapper<PersonPrivilegeEntity, PersonPrivilegeDto> {

    @Override
    public PersonPrivilegeEntity toNewEntity(PersonPrivilegeDto dto) {
        PersonPrivilegeEntity entity = new PersonPrivilegeEntity();
        entity.setDateFrom(new Date());
        toUpdatedEntity(dto, entity);
        return entity;
    }

    @Override
    public PersonPrivilegeEntity toUpdatedEntity(PersonPrivilegeDto dto, PersonPrivilegeEntity entity) {
        entity.setUpdateDate(new Date());
        entity.setExternalId(dto.getExternalId());
        return entity;
    }

    @Override
    public PersonPrivilegeDto toDto(PersonPrivilegeEntity entity) {
        PersonPrivilegeDto dto = new PersonPrivilegeDto();
        if (entity.getPrivilege() != null) {
            dto.setPrivilegeId(entity.getPrivilege().getId());
        }
        dto.setId(entity.getId());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setExternalId(entity.getExternalId());
        dto.setAuthorEmployeeId(entity.getAuthorEmployeeId());
        dto.setUpdateEmployeeId(entity.getUpdateEmployeeId());

        return dto;
    }
}
