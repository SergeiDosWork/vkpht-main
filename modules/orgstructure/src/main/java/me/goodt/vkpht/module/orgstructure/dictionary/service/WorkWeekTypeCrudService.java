package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.WorkWeekTypeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.WorkWeekTypeDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkWeekTypeEntity;

@Service
@RequiredArgsConstructor
public class WorkWeekTypeCrudService {
    private final WorkWeekTypeDao dao;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<WorkWeekTypeEntity, WorkWeekTypeDto> mapper;


    public WorkWeekTypeDto getById(Integer id) {
        WorkWeekTypeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Entity with id = " + id + " not found"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return mapper.toDto(entity);
    }

    public WorkWeekTypeDto create(WorkWeekTypeDto request) {
        WorkWeekTypeEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public WorkWeekTypeDto update(Integer id, WorkWeekTypeDto request) {
        WorkWeekTypeEntity entity = getSecured(id);
        WorkWeekTypeEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    public Page<WorkWeekTypeDto> findAll(Pageable pageable) {
        return dao.findAllByUnitCode(unitAccessService.getCurrentUnit(), pageable)
            .map(mapper::toDto);
    }

    public void delete(Integer id) {
        WorkWeekTypeEntity entity = getSecured(id);
        dao.delete(entity);
    }

    private WorkWeekTypeEntity getSecured(Integer id) {
        WorkWeekTypeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись WorkWeekTypeEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
