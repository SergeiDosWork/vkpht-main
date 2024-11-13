package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionRankFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionRankDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionRankDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.orgstructure.PositionRankMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionRankEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class PositionRankCrudService {

    private final PositionRankDao dao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final PositionRankMapper mapper;

    @Transactional(readOnly = true)
    public Page<PositionRankDto> findAll(Pageable pageable) {
        PositionRankFilter filter = PositionRankFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public PositionRankDto getById(Long id) {
        PositionRankEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("PositionRank with id = %s not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());

        return mapper.toDto(entity);
    }

    @Transactional
    public PositionRankDto create(PositionRankDto request) {
        PositionRankEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);

        return mapper.toDto(entity);
    }

    @Transactional
    public PositionRankDto update(Long id, PositionRankDto request) {
        PositionRankEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("PositionRank with id = %s not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        mapper.toUpdatedEntity(request, entity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());

        return mapper.toDto(dao.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        PositionRankEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("PositionRank with id = %s not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }
}
