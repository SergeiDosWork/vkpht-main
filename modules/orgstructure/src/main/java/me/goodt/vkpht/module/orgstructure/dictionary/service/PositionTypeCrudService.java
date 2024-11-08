package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionTypeDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionTypeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.PositionTypeMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionTypeEntity;
import com.goodt.drive.rtcore.security.AuthService;

@Service
@RequiredArgsConstructor
public class PositionTypeCrudService {

    private final PositionTypeDao dao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final PositionTypeMapper mapper;

    @Transactional(readOnly = true)
    public Page<PositionTypeDto> findAll(Pageable pageable) {
        PositionTypeFilter filter = PositionTypeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public PositionTypeDto getById(Long id) {
        PositionTypeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("PositionType with id = %s not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return mapper.toDto(entity);
    }

    @Transactional
    public PositionTypeDto create(PositionTypeDto request) {
        PositionTypeEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);

        return mapper.toDto(entity);
    }

    @Transactional
    public PositionTypeDto update(Long id, PositionTypeDto request) {
        PositionTypeEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("PositionType with id = %s not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        mapper.toUpdatedEntity(request, entity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());

        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        PositionTypeEntity entity = dao.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("PositionType with id = %s not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }
}
