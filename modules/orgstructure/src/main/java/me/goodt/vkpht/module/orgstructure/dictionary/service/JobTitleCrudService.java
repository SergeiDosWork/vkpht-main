package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import com.goodt.drive.rtcore.dao.filter.JobTitleFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.JobTitleDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.JobTitleDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.JobTitleEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class JobTitleCrudService {
    private final JobTitleDao dao;
    private final UnitAccessService unitAccessService;
    private final AuthService authService;
    private final CrudDtoMapper<JobTitleEntity, JobTitleDto> mapper;

    public JobTitleDto getById(Long id) {
        return mapper.toDto(getSecured(id));
    }

    public JobTitleDto create(JobTitleDto request) {
        JobTitleEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public JobTitleDto update(Long id, JobTitleDto request) {
        JobTitleEntity entity = getSecured(id);
        JobTitleEntity updatedEntity = mapper.toUpdatedEntity(request, entity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(updatedEntity);
        return mapper.toDto(updatedEntity);
    }

    public Page<JobTitleDto> findAll(Pageable pageable) {
        JobTitleFilter filter = JobTitleFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    public void delete(Long id) {
        JobTitleEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        dao.save(entity);
    }

    private JobTitleEntity getSecured(Long id) {
        JobTitleEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись JobTitleEntity c идентификатором = " + id + " не найдена"));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
