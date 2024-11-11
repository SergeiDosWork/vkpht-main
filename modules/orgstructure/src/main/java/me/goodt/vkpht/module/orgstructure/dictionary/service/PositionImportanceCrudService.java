package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionImportanceFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionImportanceDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionImportanceDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class PositionImportanceCrudService {

    private final PositionImportanceDao dao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<PositionImportanceEntity, PositionImportanceDto> mapper;

    public Page<PositionImportanceDto> findAll(Pageable pageable) {
        PositionImportanceFilter filter = PositionImportanceFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    public PositionImportanceDto getById(Integer id) {
        return mapper.toDto(getSecured(id));
    }
    
    public PositionImportanceDto create(PositionImportanceDto request) {
        PositionImportanceEntity entity = mapper.toNewEntity(request);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }
    
    public PositionImportanceDto update(Integer id, PositionImportanceDto request) {
        PositionImportanceEntity entity = getSecured(id);
        entity = mapper.toUpdatedEntity(request, entity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }
    
    public void delete(Integer id) {
        PositionImportanceEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private PositionImportanceEntity getSecured(Integer id) {
        PositionImportanceEntity entity = dao.findById(id)
            .orElseThrow(() -> new NotFoundException("Запись PositionImportanceEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }

}
