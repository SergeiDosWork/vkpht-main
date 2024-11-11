package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.ReasonFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.OrgReasonDao;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ReasonDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonEntity;

@Service
@RequiredArgsConstructor
public class OrgReasonCrudService {
    private final OrgReasonDao dao;
    private final UnitAccessService unitAccessService;
    private final CrudDtoMapper<OrgReasonEntity, ReasonDto> mapper;

    public Page<ReasonDto> findAll(Pageable pageable) {
        ReasonFilter filter = ReasonFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return dao.find(filter, pageable).map(mapper::toDto);
    }

    public ReasonDto getById(Long id) {
        return mapper.toDto(getSecured(id));
    }

    public ReasonDto create(ReasonDto request) {
        OrgReasonEntity entity = mapper.toNewEntity(request);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }
    
    public ReasonDto update(Long id, ReasonDto request) {
        OrgReasonEntity entity = mapper.toUpdatedEntity(request, getSecured(id));
        entity = dao.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id) {
        OrgReasonEntity entity = getSecured(id);
        entity.setDateTo(new Date());
        dao.save(entity);
    }

    private OrgReasonEntity getSecured(Long id) {
        var filter = ReasonFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        OrgReasonEntity entity = dao.findOne(id, filter).orElseThrow(() ->
            new NotFoundException("Запись OrgReasonEntity c идентификатором = " + id + " не найдена"));
        return entity;
    }
}
