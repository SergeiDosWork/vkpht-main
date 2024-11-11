package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import me.goodt.vkpht.module.orgstructure.domain.dao.JobTitleClusterDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.JobTitleClusterDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.JobTitleClusterEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
@RequiredArgsConstructor
public class JobTitleClusterCrudService {

    private final JobTitleClusterDao dao;
    private final CrudDtoMapper<JobTitleClusterEntity, JobTitleClusterDto> mapper;
    private final AuthService authService;

    public Page<JobTitleClusterDto> findAll(Pageable pageable) {
        return dao.findAll(pageable).map(mapper::toDto);
    }

    public JobTitleClusterDto getById(Long id) {
        return mapper.toDto(getSecured(id));
    }

    public JobTitleClusterDto create(JobTitleClusterDto request) {
        JobTitleClusterEntity entity = mapper.toNewEntity(request);
        entity.setAuthorEmployeeId(authService.getUserEmployeeId());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public JobTitleClusterDto update(Long id, JobTitleClusterDto request) {
        JobTitleClusterEntity entity = getSecured(id);
        entity = mapper.toUpdatedEntity(request, entity);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id) {
        JobTitleClusterEntity entity = getSecured(id);
        dao.delete(entity);
    }

    private JobTitleClusterEntity getSecured(Long id) {
        JobTitleClusterEntity entity = dao.findById(id).orElseThrow(() ->
            new NotFoundException("Запись JobTitleClusterEntity c идентификатором = " + id + " не найдена"));
        return entity;
    }
}
