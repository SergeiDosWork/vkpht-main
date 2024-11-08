package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceCriteriaDto;
import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceCriteriaGroupDto;
import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceCriteriaGroupTypeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionImportanceCriteriaDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.CalculationMethodEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceCriteriaEntity;

public interface IImportanceCriteriaService {
    ImportanceCriteriaGroupTypeEntity findImportanceCriteriaGroupType(Long id) throws NotFoundException;

    List<ImportanceCriteriaGroupTypeEntity> findImportanceCriteriaGroupTypeAll();

    ImportanceCriteriaGroupTypeEntity createImportanceCriteriaGroupType(ImportanceCriteriaGroupTypeDto dto);

    ImportanceCriteriaGroupTypeEntity updateImportanceCriteriaGroupType(ImportanceCriteriaGroupTypeDto dto) throws NotFoundException;

    Long deleteImportanceCriteriaGroupType(Long id) throws NotFoundException;

    ImportanceCriteriaGroupEntity findImportanceCriteriaGroup(Long id) throws NotFoundException;

    List<ImportanceCriteriaGroupEntity> findImportanceCriteriaGroupAll();

    ImportanceCriteriaGroupEntity createImportanceCriteriaGroup(ImportanceCriteriaGroupDto dto) throws NotFoundException;

    ImportanceCriteriaGroupEntity updateImportanceCriteriaGroup(ImportanceCriteriaGroupDto dto) throws NotFoundException;

    Long deleteImportanceCriteriaGroup(Long id) throws NotFoundException;

    ImportanceCriteriaEntity findImportanceCriteria(Long id) throws NotFoundException;

    List<ImportanceCriteriaEntity> findImportanceCriteriaAll();

    ImportanceCriteriaEntity createImportanceCriteria(ImportanceCriteriaDto dto) throws NotFoundException;

    ImportanceCriteriaEntity updateImportanceCriteria(ImportanceCriteriaDto dto) throws NotFoundException;

    Long deleteImportanceCriteria(Long id) throws NotFoundException;

    PositionImportanceCriteriaEntity findPositionImportanceCriteria(Long id) throws NotFoundException;

    List<PositionImportanceCriteriaEntity> findPositionImportanceCriteriaAll();

    PositionImportanceCriteriaEntity createPositionImportanceCriteria(PositionImportanceCriteriaDto dto) throws NotFoundException;

    PositionImportanceCriteriaEntity updatePositionImportanceCriteria(PositionImportanceCriteriaDto dto) throws NotFoundException;

    Long deletePositionImportanceCriteria(Long id) throws NotFoundException;

    List<PositionImportanceCriteriaEntity> recalculateImportanceCriteria(Long importanceCriteriaId, Long positionId, float value) throws NotFoundException;

    CalculationMethodEntity findCalculationMethod(Long id) throws NotFoundException;
}
