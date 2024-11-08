package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionKrLevelDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionKrLevelDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionKrLevelEntity;

@Service
@RequiredArgsConstructor
public class PositionKrLevelCrudService {

    private final PositionKrLevelDao dao;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<PositionKrLevelEntity, PositionKrLevelDto> mapper;

    @Transactional(readOnly = true)
    public PositionKrLevelDto getById(Long id) {
        PositionKrLevelEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<PositionKrLevelDto> findAll(Pageable pageable) {
        return dao.findAllByUnitCode(unitAccessService.getCurrentUnit(), pageable)
            .map(mapper::toDto);
    }

    @Transactional
    public PositionKrLevelDto update(Long id, PositionKrLevelDto request) {
        PositionKrLevelEntity entity = getSecured(id);
        PositionKrLevelEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public PositionKrLevelDto create(PositionKrLevelDto request) {
        PositionKrLevelEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        PositionKrLevelEntity entity = getSecured(id);
        dao.delete(entity);
    }

    private PositionKrLevelEntity getSecured(Long id) {
        PositionKrLevelEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись IndicatorScaleTemplateEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return entity;
    }
}
