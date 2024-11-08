package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentCategoryFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentCategoryDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentCategoryDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentCategoryEntity;
import com.goodt.drive.rtcore.security.AuthService;

@Service
@RequiredArgsConstructor
public class AssignmentCategoryCrudService {

    private final AssignmentCategoryDao dao;
    private final AuthService authService;
    private final CrudDtoMapper<AssignmentCategoryEntity, AssignmentCategoryDto> mapper;
    private final UnitAccessService unitAccessService;

    public Page<AssignmentCategoryDto> findAll(Pageable pageable) {
        var filter = AssignmentCategoryFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();

        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public AssignmentCategoryDto getById(Long id) {
        AssignmentCategoryEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional
    public AssignmentCategoryDto create(AssignmentCategoryDto createRequest) {
        AssignmentCategoryEntity entity = mapper.toNewEntity(createRequest);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        this.dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public AssignmentCategoryDto update(Long id, AssignmentCategoryDto updateRequest) {
        AssignmentCategoryEntity entity = getSecured(id);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        AssignmentCategoryEntity updatedEntity = mapper.toUpdatedEntity(updateRequest, entity);
        this.dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        AssignmentCategoryEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateDate(new Date());
        dao.save(entity);
    }

    private AssignmentCategoryEntity getSecured(Long id) {
        AssignmentCategoryEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись AssignmentCategoryEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
