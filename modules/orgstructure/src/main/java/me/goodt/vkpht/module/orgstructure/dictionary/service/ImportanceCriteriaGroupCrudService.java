package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.ImportanceCriteriaGroupDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ImportanceCriteriaGroupDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupEntity;

@Service
@RequiredArgsConstructor
public class ImportanceCriteriaGroupCrudService {
    private final ImportanceCriteriaGroupDao dao;
    private final CrudDtoMapper<ImportanceCriteriaGroupEntity, ImportanceCriteriaGroupDto> mapper;
    private final UnitAccessService unitAccessService;

    public Page<ImportanceCriteriaGroupDto> findAll(Pageable pageable) {
        return dao.find(unitAccessService.getCurrentUnit(), pageable).map(mapper::toDto);
    }

    public ImportanceCriteriaGroupDto getById(Long id) {
        return mapper.toDto(getSecured(id));
    }

    public ImportanceCriteriaGroupDto create(ImportanceCriteriaGroupDto request) {
        ImportanceCriteriaGroupEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }
    
    public ImportanceCriteriaGroupDto update(Long id, ImportanceCriteriaGroupDto request) {
        ImportanceCriteriaGroupEntity entity = getSecured(id);
        entity = mapper.toUpdatedEntity(request, entity);
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id) {
        ImportanceCriteriaGroupEntity entity = getSecured(id);
        dao.delete(entity);
    }

    private ImportanceCriteriaGroupEntity getSecured(Long id) {
        ImportanceCriteriaGroupEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись ImportanceCriteriaGroupEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
