package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionStatusDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionStatusDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionStatusEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class PositionStatusCrudService {

    private final PositionStatusDao dao;

    private final UnitAccessService unitAccessService;

    private final CrudDtoMapper<PositionStatusEntity, PositionStatusDto> mapper;

    private final AuthService authService;

    @Transactional
    public PositionStatusDto update(Integer id, PositionStatusDto request) {
        PositionStatusEntity entity = getSecured(id);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        PositionStatusEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    @Transactional
    public PositionStatusDto create(PositionStatusDto request) {
        PositionStatusEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Integer id) {
        PositionStatusEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        dao.save(entity);
    }

    @Transactional(readOnly = true)
    public PositionStatusDto getById(Integer id) {
        PositionStatusEntity entity = getSecured(id);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<PositionStatusDto> findAll(Pageable pageable) {
        var filter = PositionStatusFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable)
            .map(mapper::toDto);
    }

    private PositionStatusEntity getSecured(Integer id) {
        PositionStatusEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись PositionStatusEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return entity;
    }
}
