package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.WorkAwardTypeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.WorkAwardTypeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkAwardTypeEntity;

@Service
@RequiredArgsConstructor
public class WorkAwardTypeCrudService {

    private final WorkAwardTypeDao dao;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<WorkAwardTypeEntity, WorkAwardTypeDto> mapper;

    @Transactional(readOnly = true)
    public WorkAwardTypeDto getById(Long id) {
        WorkAwardTypeEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<WorkAwardTypeDto> findAll(Pageable pageable) {
        return dao.findAllByUnitCode(unitAccessService.getCurrentUnit(), pageable)
            .map(mapper::toDto);
    }

    @Transactional
    public WorkAwardTypeDto update(Long id, WorkAwardTypeDto request) {
        WorkAwardTypeEntity entity = getSecured(id);
        WorkAwardTypeEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public WorkAwardTypeDto create(WorkAwardTypeDto request) {
        WorkAwardTypeEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        WorkAwardTypeEntity entity = getSecured(id);
        dao.delete(entity);
    }

    private WorkAwardTypeEntity getSecured(Long id) {
        WorkAwardTypeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись WorkAwardTypeEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return entity;
    }
}
