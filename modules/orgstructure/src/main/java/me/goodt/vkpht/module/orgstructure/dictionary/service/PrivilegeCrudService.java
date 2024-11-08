package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PrivilegeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.PrivilegeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PrivilegeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PrivilegeEntity;
import com.goodt.drive.rtcore.security.AuthService;

@Service
@RequiredArgsConstructor
public class PrivilegeCrudService {

    private final PrivilegeDao dao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<PrivilegeEntity, PrivilegeDto> mapper;

    @Transactional
    public PrivilegeDto update(Long id, PrivilegeDto request) {
        PrivilegeEntity entity = getSecured(id);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        PrivilegeEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public PrivilegeDto create(PrivilegeDto request) {
        PrivilegeEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        PrivilegeEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    @Transactional(readOnly = true)
    public PrivilegeDto getById(Long id) {
        PrivilegeEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<PrivilegeDto> findAll(Pageable pageable) {
        var filter = PrivilegeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    private PrivilegeEntity getSecured(Long id) {
        PrivilegeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись PrivilegeEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return entity;
    }
}
