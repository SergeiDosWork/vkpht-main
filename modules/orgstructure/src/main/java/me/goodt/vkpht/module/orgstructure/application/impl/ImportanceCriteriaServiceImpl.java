package me.goodt.vkpht.module.orgstructure.application.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.ImportanceCriteriaService;
import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceCriteriaDto;
import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceCriteriaGroupDto;
import me.goodt.vkpht.module.orgstructure.api.dto.ImportanceCriteriaGroupTypeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionImportanceCriteriaDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.CalculationMethodDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.ImportanceCriteriaDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.ImportanceCriteriaGroupDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.ImportanceCriteriaGroupTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionImportanceCriteriaDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.CalculationMethodEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceCriteriaEntity;

@Service
public class ImportanceCriteriaServiceImpl implements ImportanceCriteriaService {

    @Autowired
    private CalculationMethodDao calculationMethodDao;
    @Autowired
    private ImportanceCriteriaGroupDao importanceCriteriaGroupDao;
    @Autowired
    private ImportanceCriteriaGroupTypeDao importanceCriteriaGroupTypeDao;
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private ImportanceCriteriaDao importanceCriteriaDao;
    @Autowired
    private PositionImportanceCriteriaDao positionImportanceCriteriaDao;
    @Autowired
    private UnitAccessService unitAccessService;

    @Override
    public ImportanceCriteriaGroupTypeEntity findImportanceCriteriaGroupType(Long id) throws NotFoundException {
        return importanceCriteriaGroupTypeDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("ImportanceCriteriaGroupType with id %d not found", id)));
    }

    @Override
    public List<ImportanceCriteriaGroupTypeEntity> findImportanceCriteriaGroupTypeAll() {
        return StreamSupport.stream(importanceCriteriaGroupTypeDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ImportanceCriteriaGroupTypeEntity createImportanceCriteriaGroupType(ImportanceCriteriaGroupTypeDto dto) {
        ImportanceCriteriaGroupTypeEntity entity = new ImportanceCriteriaGroupTypeEntity(dto.getId(), dto.getName());
        return importanceCriteriaGroupTypeDao.save(entity);
    }

    @Override
    public ImportanceCriteriaGroupTypeEntity updateImportanceCriteriaGroupType(ImportanceCriteriaGroupTypeDto dto) throws NotFoundException {
        ImportanceCriteriaGroupTypeEntity entity = findImportanceCriteriaGroupType(dto.getId());
        entity.setName(dto.getName());
        return importanceCriteriaGroupTypeDao.save(entity);
    }

    @Override
    public Long deleteImportanceCriteriaGroupType(Long id) throws NotFoundException {
        ImportanceCriteriaGroupTypeEntity entity = findImportanceCriteriaGroupType(id);
        importanceCriteriaGroupTypeDao.delete(entity);
        return 1L;
    }

    @Override
    public ImportanceCriteriaGroupEntity findImportanceCriteriaGroup(Long id) throws NotFoundException {
        return importanceCriteriaGroupDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("ImportanceCriteriaGroup with id %d not found", id)));
    }

    @Override
    public List<ImportanceCriteriaGroupEntity> findImportanceCriteriaGroupAll() {
        return StreamSupport.stream(importanceCriteriaGroupDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ImportanceCriteriaGroupEntity createImportanceCriteriaGroup(ImportanceCriteriaGroupDto dto) throws NotFoundException {
        ImportanceCriteriaGroupTypeEntity importanceCriteriaGroupTypeEntity = findImportanceCriteriaGroupType(dto.getTypeId());
        ImportanceCriteriaGroupEntity entity = new ImportanceCriteriaGroupEntity(importanceCriteriaGroupTypeEntity, dto.getName(), dto.getDescription(), dto.getIsEditable());
        return importanceCriteriaGroupDao.save(entity);
    }

    @Override
    public ImportanceCriteriaGroupEntity updateImportanceCriteriaGroup(ImportanceCriteriaGroupDto dto) throws NotFoundException {
        ImportanceCriteriaGroupEntity entity = findImportanceCriteriaGroup(dto.getId());
        ImportanceCriteriaGroupTypeEntity importanceCriteriaGroupTypeEntity = findImportanceCriteriaGroupType(dto.getTypeId());
        entity.setTypeId(importanceCriteriaGroupTypeEntity);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIsEditable(dto.getIsEditable());
        return importanceCriteriaGroupDao.save(entity);
    }

    @Override
    public Long deleteImportanceCriteriaGroup(Long id) throws NotFoundException {
        ImportanceCriteriaGroupEntity entity = findImportanceCriteriaGroup(id);
        importanceCriteriaGroupDao.delete(entity);
        return 1L;
    }

    @Override
    public ImportanceCriteriaEntity findImportanceCriteria(Long id) throws NotFoundException {
        return importanceCriteriaDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("ImportanceCriteria with id %d not found", id)));
    }

    @Override
    public List<ImportanceCriteriaEntity> findImportanceCriteriaAll() {
        return StreamSupport.stream(importanceCriteriaDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ImportanceCriteriaEntity createImportanceCriteria(ImportanceCriteriaDto dto) throws NotFoundException {
        ImportanceCriteriaGroupEntity importanceCriteriaGroupEntity = findImportanceCriteriaGroup(dto.getGroupId());
        ImportanceCriteriaEntity entity = new ImportanceCriteriaEntity();
        entity.setGroup(importanceCriteriaGroupEntity);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWeight(dto.getWeight());
        entity.setIsEnabled(dto.getIsEnabled());
        if (dto.getCalculationMethod() != null && dto.getCalculationMethod().getId() != null) {
            entity.setCalculationMethod(findCalculationMethod(dto.getCalculationMethod().getId()));
        }
        return importanceCriteriaDao.save(entity);
    }

    @Override
    public ImportanceCriteriaEntity updateImportanceCriteria(ImportanceCriteriaDto dto) throws NotFoundException {
        ImportanceCriteriaGroupEntity importanceCriteriaGroupEntity = findImportanceCriteriaGroup(dto.getGroupId());

        ImportanceCriteriaEntity entity = findImportanceCriteria(dto.getId());
        entity.setGroup(importanceCriteriaGroupEntity);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWeight(dto.getWeight());
        entity.setIsEnabled(dto.getIsEnabled());
        if (dto.getCalculationMethod() != null && dto.getCalculationMethod().getId() != null) {
            entity.setCalculationMethod(findCalculationMethod(dto.getCalculationMethod().getId()));
        }
        return importanceCriteriaDao.save(entity);
    }

    @Override
    public Long deleteImportanceCriteria(Long id) throws NotFoundException {
        ImportanceCriteriaEntity entity = findImportanceCriteria(id);
        importanceCriteriaDao.delete(entity);
        return 1L;
    }

    @Override
    public PositionImportanceCriteriaEntity findPositionImportanceCriteria(Long id) throws NotFoundException {
        return positionImportanceCriteriaDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("PositionImportanceCriteria with id %d not found", id)));
    }

    @Override
    public List<PositionImportanceCriteriaEntity> findPositionImportanceCriteriaAll() {
        return StreamSupport.stream(positionImportanceCriteriaDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PositionImportanceCriteriaEntity createPositionImportanceCriteria(PositionImportanceCriteriaDto dto) throws NotFoundException {
        ImportanceCriteriaEntity importanceCriteriaEntity = findImportanceCriteria(dto.getImportanceCriteriaId());
        PositionEntity positionEntity = getPosition(dto.getPositionId());
        PositionImportanceCriteriaEntity entity = new PositionImportanceCriteriaEntity(new Date(), null, positionEntity, importanceCriteriaEntity, dto.getWeight(), dto.getValue());
        return positionImportanceCriteriaDao.save(entity);
    }

    @Override
    public PositionImportanceCriteriaEntity updatePositionImportanceCriteria(PositionImportanceCriteriaDto dto) throws NotFoundException {
        PositionImportanceCriteriaEntity entity = findPositionImportanceCriteria(dto.getId());
        ImportanceCriteriaEntity importanceCriteriaEntity = findImportanceCriteria(dto.getImportanceCriteriaId());
        PositionEntity positionEntity = getPosition(dto.getPositionId());
        entity.setPosition(positionEntity);
        entity.setImportanceCriteria(importanceCriteriaEntity);
        entity.setWeight(dto.getWeight());
        entity.setValue(dto.getValue());
        return positionImportanceCriteriaDao.save(entity);
    }

    @Override
    public Long deletePositionImportanceCriteria(Long id) throws NotFoundException {
        PositionImportanceCriteriaEntity entity = findPositionImportanceCriteria(id);
        entity.setDateTo(new Date());
        positionImportanceCriteriaDao.save(entity);
        return 1L;
    }

    private PositionEntity getPosition(Long id) {
        if (id == null) {
            return null;
        }
        Optional<PositionEntity> position = positionDao.findById(id);
        if (!position.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "cannot find position with id = " + id);
        }
        PositionEntity entity = position.get();
        if (entity.getPositionImportance() != null) {
            unitAccessService.checkUnitAccess(entity.getPositionImportance().getUnitCode());
        }
        return position.get();
    }

    @Transactional
    @Override
    public List<PositionImportanceCriteriaEntity> recalculateImportanceCriteria(Long importanceCriteriaId, Long positionId, float value) throws NotFoundException {
        List<PositionImportanceCriteriaEntity> list = positionImportanceCriteriaDao.findAllByPositionIdAndImportanceCriteriaId(positionId, importanceCriteriaId);
        List<PositionImportanceCriteriaEntity> result = new ArrayList<>();
        if (list.isEmpty()) {
            PositionImportanceCriteriaEntity entity = createPositionImportanceCriteria(new PositionImportanceCriteriaDto(null, new Date(), null, positionId, importanceCriteriaId, 0, value));
            result.add(entity);
        } else {
            for (PositionImportanceCriteriaEntity entity : list) {
                if (entity.getValue() != value) {
                    PositionImportanceCriteriaEntity entity1 = createPositionImportanceCriteria(new PositionImportanceCriteriaDto(null, new Date(), null, positionId, importanceCriteriaId, 0, value));
                    deletePositionImportanceCriteria(entity.getId());
                    result.add(entity1);
                }
            }
        }
        return result;
    }

    @Override
    public CalculationMethodEntity findCalculationMethod(Long id) throws NotFoundException {
        return calculationMethodDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("CalculationMethod with id %d not found", id)));
    }
}
