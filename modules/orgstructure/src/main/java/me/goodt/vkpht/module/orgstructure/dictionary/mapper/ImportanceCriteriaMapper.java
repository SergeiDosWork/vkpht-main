package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.domain.dao.CalculationMethodDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.ImportanceCriteriaGroupDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ImportanceCriteriaDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaEntity;

@RequiredArgsConstructor
@Component
public class ImportanceCriteriaMapper implements CrudDtoMapper<ImportanceCriteriaEntity, ImportanceCriteriaDto> {
    private final ImportanceCriteriaGroupDao importanceCriteriaGroupDao;
    private final CalculationMethodDao calculationMethodDao;
    @Override
    public ImportanceCriteriaEntity toNewEntity(ImportanceCriteriaDto dto) {
        ImportanceCriteriaEntity entity = new ImportanceCriteriaEntity();
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public ImportanceCriteriaEntity toUpdatedEntity(ImportanceCriteriaDto dto, ImportanceCriteriaEntity entity) {
        if (dto.getGroupId() != null) {
            entity.setGroup(importanceCriteriaGroupDao.findById(dto.getGroupId()).orElseThrow(() ->
                new NotFoundException(String.format("Group id = %d not found", dto.getGroupId()))));
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWeight(dto.getWeight());
        entity.setIsEnabled(dto.getIsEnabled());
        if (dto.getCalculationMethodId() != null) {
            entity.setCalculationMethod(calculationMethodDao.findById(dto.getCalculationMethodId()).orElseThrow(() ->
                new NotFoundException(String.format("CalculationMethod id = %d not found",
                    dto.getCalculationMethodId()))));
        } else {
            entity.setCalculationMethod(null);
        }
        return entity;
    }

    @Override
    public ImportanceCriteriaDto toDto(ImportanceCriteriaEntity entity) {
        ImportanceCriteriaDto dto = new ImportanceCriteriaDto();
        dto.setId(entity.getId());
        if (entity.getGroup() != null) {
            dto.setGroupId(entity.getGroup().getId());
        }
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setWeight(entity.getWeight());
        dto.setIsEnabled(entity.getIsEnabled());
        if (entity.getCalculationMethod() != null) {
            dto.setCalculationMethodId(entity.getCalculationMethod().getId());
        }

        return dto;
    }
}
