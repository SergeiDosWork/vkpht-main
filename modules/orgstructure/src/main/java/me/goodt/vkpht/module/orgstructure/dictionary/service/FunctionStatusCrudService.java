package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.FunctionStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.FunctionStatusDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.FunctionStatusDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.FunctionStatusEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class FunctionStatusCrudService {
    private final FunctionStatusDao dao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<FunctionStatusEntity, FunctionStatusDto> mapper;

    public Page<FunctionStatusDto> findAll(Pageable pageable) {
        FunctionStatusFilter filter = FunctionStatusFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    public FunctionStatusDto get(Integer id) {
        return mapper.toDto(getSecured(id));
    }

    public FunctionStatusDto create(FunctionStatusDto request) {
        FunctionStatusEntity entity = mapper.toNewEntity(request);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        dao.save(entity);
        return mapper.toDto(entity);
    }

    public FunctionStatusDto update(Integer id, FunctionStatusDto request) {
        FunctionStatusEntity entity = mapper.toUpdatedEntity(request, getSecured(id));
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Integer id) {
        FunctionStatusEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private FunctionStatusEntity getSecured(Integer id) {
        FunctionStatusEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись FunctionStatusEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
