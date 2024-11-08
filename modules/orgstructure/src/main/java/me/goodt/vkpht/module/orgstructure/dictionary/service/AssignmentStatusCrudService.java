package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentStatusDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentStatusDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentStatusEntity;
import com.goodt.drive.rtcore.security.AuthService;

@Service
@RequiredArgsConstructor
public class AssignmentStatusCrudService {

    private final AssignmentStatusDao dao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<AssignmentStatusEntity, AssignmentStatusDto> mapper;

    @Transactional
    public AssignmentStatusDto update(Integer id, AssignmentStatusDto request) {
        AssignmentStatusEntity entity = getSecured(id);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        AssignmentStatusEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public AssignmentStatusDto create(AssignmentStatusDto request) {
        AssignmentStatusEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Integer id) {
        AssignmentStatusEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        dao.save(entity);
    }

    @Transactional(readOnly = true)
    public AssignmentStatusDto getById(Integer id) {
        AssignmentStatusEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<AssignmentStatusDto> findAll(Pageable pageable) {
        var filter = AssignmentStatusFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    private AssignmentStatusEntity getSecured(Integer id) {
        AssignmentStatusEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись AssignmentStatusEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return entity;
    }
}
