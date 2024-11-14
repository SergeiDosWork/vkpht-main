package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentTypeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentTypeDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentTypeEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class AssignmentTypeCrudService {

    private final AssignmentTypeDao dao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<AssignmentTypeEntity, AssignmentTypeDto> mapper;

    @Transactional
    public AssignmentTypeDto update(Integer id, AssignmentTypeDto request) {
        AssignmentTypeEntity entity = getSecured(id);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        AssignmentTypeEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public AssignmentTypeDto create(AssignmentTypeDto request) {
        AssignmentTypeEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Integer id) {
        AssignmentTypeEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    @Transactional(readOnly = true)
    public AssignmentTypeDto getById(Integer id) {
        AssignmentTypeEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<AssignmentTypeDto> findAll(Pageable pageable) {
        var filter = AssignmentTypeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    private AssignmentTypeEntity getSecured(Integer id) {
        AssignmentTypeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись AssignmentStatusEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return entity;
    }
}
