package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentReadinessFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentReadinessDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentReadinessDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentReadinessEntity;

@Service
@RequiredArgsConstructor
public class AssignmentReadinessCrudService {
    private final AssignmentReadinessDao dao;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<AssignmentReadinessEntity, AssignmentReadinessDto> mapper;

    public Page<AssignmentReadinessDto> findAll(Pageable pageable) {
        AssignmentReadinessFilter filter = AssignmentReadinessFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }
    
    public AssignmentReadinessDto getById(Integer id) {
        return mapper.toDto(getSecured(id));
    }

    public AssignmentReadinessDto create(AssignmentReadinessDto request) {
        AssignmentReadinessEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public AssignmentReadinessDto update(Integer id, AssignmentReadinessDto request) {
        AssignmentReadinessEntity entity = getSecured(id);
        entity = mapper.toUpdatedEntity(request, entity);
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }
    
    public void delete(Integer id) {
        AssignmentReadinessEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        dao.save(entity);
    }

    private AssignmentReadinessEntity getSecured(Integer id) {
        AssignmentReadinessEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись AssignmentReadinessEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
