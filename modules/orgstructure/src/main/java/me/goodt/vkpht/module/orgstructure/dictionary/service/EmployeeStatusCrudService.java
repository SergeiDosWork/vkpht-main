package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.EmployeeStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeStatusDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.EmployeeStatusDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeStatusEntity;
import com.goodt.drive.rtcore.security.AuthService;

@Service
@RequiredArgsConstructor
public class EmployeeStatusCrudService {

    private final EmployeeStatusDao dao;
    private final AuthService authService;
    private final CrudDtoMapper<EmployeeStatusEntity, EmployeeStatusDto> mapper;
    private final UnitAccessService unitAccessService;

    public Page<EmployeeStatusDto> findAll(Pageable pageable) {
        var filter = EmployeeStatusFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();

        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public EmployeeStatusDto getById(Long id) {
        EmployeeStatusEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional
    public EmployeeStatusDto create(EmployeeStatusDto createRequest) {
        EmployeeStatusEntity entity = mapper.toNewEntity(createRequest);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        this.dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public EmployeeStatusDto update(Long id, EmployeeStatusDto updateRequest) {
        EmployeeStatusEntity entity = getSecured(id);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        EmployeeStatusEntity updatedEntity = mapper.toUpdatedEntity(updateRequest, entity);
        this.dao.save(updatedEntity);

        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        EmployeeStatusEntity entity = getSecured(id);
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private EmployeeStatusEntity getSecured(Long id) {
        EmployeeStatusEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись EmployeeStatusEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
