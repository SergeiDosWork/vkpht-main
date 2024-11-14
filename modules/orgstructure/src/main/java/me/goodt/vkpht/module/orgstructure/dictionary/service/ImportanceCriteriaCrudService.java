package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.ImportanceCriteriaDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ImportanceCriteriaDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaEntity;

@Service
@RequiredArgsConstructor
public class ImportanceCriteriaCrudService {
    private final ImportanceCriteriaDao dao;
    private final CrudDtoMapper<ImportanceCriteriaEntity, ImportanceCriteriaDto> mapper;
    private final UnitAccessService unitAccessService;

    public Page<ImportanceCriteriaDto> findAll(Pageable pageable) {
        return dao.find(unitAccessService.getCurrentUnit(), pageable).map(mapper::toDto);
    }

    public ImportanceCriteriaDto getById(Long id) {
        return mapper.toDto(getSecured(id));
    }

    public ImportanceCriteriaDto create(ImportanceCriteriaDto request) {
        ImportanceCriteriaEntity entity = mapper.toNewEntity(request);
        unitAccessService.checkUnitAccess(entity.getGroup().getUnitCode());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public ImportanceCriteriaDto update(Long id, ImportanceCriteriaDto request) {
        ImportanceCriteriaEntity entity = getSecured(id);
        entity = mapper.toUpdatedEntity(request, entity);
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id) {
        ImportanceCriteriaEntity entity = getSecured(id);
        dao.delete(entity);
    }

    private ImportanceCriteriaEntity getSecured(Long id) {
        ImportanceCriteriaEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись ImportanceCriteriaEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getGroup().getUnitCode());
        return entity;
    }
}
