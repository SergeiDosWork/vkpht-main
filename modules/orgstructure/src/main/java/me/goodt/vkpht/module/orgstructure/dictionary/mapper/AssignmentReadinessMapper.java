package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentReadinessDto;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentReadinessEntity;

@Component
public class AssignmentReadinessMapper implements CrudDtoMapper<AssignmentReadinessEntity, AssignmentReadinessDto> {
    @Override
    public AssignmentReadinessEntity toNewEntity(AssignmentReadinessDto dto) {
        AssignmentReadinessEntity entity = new AssignmentReadinessEntity();
        entity.setDateFrom(new Date());
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public AssignmentReadinessEntity toUpdatedEntity(AssignmentReadinessDto dto, AssignmentReadinessEntity entity) {
        entity.setName(dto.getName());
        return entity;
    }

    @Override
    public AssignmentReadinessDto toDto(AssignmentReadinessEntity entity) {
        AssignmentReadinessDto dto = new AssignmentReadinessDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateFrom(entity.getDateFrom());
        dto.setDateTo(entity.getDateTo());

        return dto;
    }
}
