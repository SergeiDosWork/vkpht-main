package me.goodt.vkpht.module.orgstructure.dictionary.service;

import com.goodt.drive.rtcore.security.AuthService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.WorkFunctionStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.WorkFunctionStatusDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.WorkFunctionStatusDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkFunctionStatusEntity;

@Service
@RequiredArgsConstructor
public class WorkFunctionStatusCrudService {
	@Getter
	private final WorkFunctionStatusDao dao;
    private final UnitAccessService unitAccessService;
    private final AuthService authService;
    private final CrudDtoMapper<WorkFunctionStatusEntity, WorkFunctionStatusDto> mapper;

    public WorkFunctionStatusDto getById(Integer id) {
        return mapper.toDto(getSecured(id));
    }

    public WorkFunctionStatusDto create(WorkFunctionStatusDto request) {
        WorkFunctionStatusEntity entity = mapper.toNewEntity(request);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public WorkFunctionStatusDto update(Integer id, WorkFunctionStatusDto request) {
        WorkFunctionStatusEntity entity = getSecured(id);
        WorkFunctionStatusEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    public Page<WorkFunctionStatusDto> findAll(Pageable pageable) {
        var filter = WorkFunctionStatusFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    public void delete(Integer id) {
        WorkFunctionStatusEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private WorkFunctionStatusEntity getSecured(Integer id) {
        WorkFunctionStatusEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись WorkFunctionStatusEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
