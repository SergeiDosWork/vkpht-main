package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentRotationFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentRotationDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentRotationDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentRotationEntity;

@Service
@RequiredArgsConstructor
public class AssignmentRotationCrudService {
    private final AssignmentRotationDao dao;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<AssignmentRotationEntity, AssignmentRotationDto> mapper;

    public Page<AssignmentRotationDto> findAll(Pageable pageable) {
        AssignmentRotationFilter filter = AssignmentRotationFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    public AssignmentRotationDto getById(Integer id) {
        return mapper.toDto(getSecured(id));
    }
    
    public AssignmentRotationDto create(AssignmentRotationDto request) {
        AssignmentRotationEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public AssignmentRotationDto update(Integer id, AssignmentRotationDto request) {
        AssignmentRotationEntity entity = getSecured(id);
        entity = mapper.toUpdatedEntity(request, entity);
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Integer id) {
        AssignmentRotationEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        dao.save(entity);
    }

    private AssignmentRotationEntity getSecured(Integer id) {
        AssignmentRotationEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись AssignmentRotationEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
