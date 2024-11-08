package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.WorkExperienceTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.WorkExperienceTypeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.WorkExperienceTypeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkExperienceTypeEntity;
import com.goodt.drive.rtcore.security.AuthService;

@Service
@RequiredArgsConstructor
public class WorkExperienceTypeCrudService {

    private final WorkExperienceTypeDao dao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<WorkExperienceTypeEntity, WorkExperienceTypeDto> mapper;

    @Transactional
    public WorkExperienceTypeDto update(Long id, WorkExperienceTypeDto request) {
        WorkExperienceTypeEntity entity = getSecured(id);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        WorkExperienceTypeEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public WorkExperienceTypeDto create(WorkExperienceTypeDto request) {
        WorkExperienceTypeEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        WorkExperienceTypeEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    @Transactional(readOnly = true)
    public WorkExperienceTypeDto getById(Long id) {
        WorkExperienceTypeEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<WorkExperienceTypeDto> findAll(Pageable pageable) {
        var filter = WorkExperienceTypeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    private WorkExperienceTypeEntity getSecured(Long id) {
        WorkExperienceTypeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись WorkExperienceTypeEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return entity;
    }
}
