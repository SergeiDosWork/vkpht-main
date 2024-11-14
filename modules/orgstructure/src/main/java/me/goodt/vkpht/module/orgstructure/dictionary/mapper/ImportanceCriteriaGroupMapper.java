package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.domain.dao.ImportanceCriteriaGroupTypeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ImportanceCriteriaGroupDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupEntity;

@Component
@RequiredArgsConstructor
public class ImportanceCriteriaGroupMapper implements CrudDtoMapper<ImportanceCriteriaGroupEntity, ImportanceCriteriaGroupDto> {
    private final ImportanceCriteriaGroupTypeDao importanceCriteriaGroupTypeDao;

    @Override
    public ImportanceCriteriaGroupEntity toNewEntity(ImportanceCriteriaGroupDto dto) {
        ImportanceCriteriaGroupEntity entity = new ImportanceCriteriaGroupEntity();
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public ImportanceCriteriaGroupEntity toUpdatedEntity(ImportanceCriteriaGroupDto dto, ImportanceCriteriaGroupEntity entity) {
        if (dto.getTypeId() != null) {
            entity.setTypeId(importanceCriteriaGroupTypeDao.findById(dto.getTypeId()).orElseThrow(() ->
                new NotFoundException(String.format("Type id = %d not found", dto.getTypeId()))));
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIsEditable((short) (Boolean.TRUE.equals(dto.getIsEditable()) ? 1 : 0));
        return entity;
    }

    @Override
    public ImportanceCriteriaGroupDto toDto(ImportanceCriteriaGroupEntity entity) {
        ImportanceCriteriaGroupDto dto = new ImportanceCriteriaGroupDto();
        dto.setId(entity.getId());
        if (entity.getTypeId() != null) {
            dto.setTypeId(entity.getTypeId().getId());
        }
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setIsEditable(entity.getIsEditable() == 1);

        return dto;
    }
}
