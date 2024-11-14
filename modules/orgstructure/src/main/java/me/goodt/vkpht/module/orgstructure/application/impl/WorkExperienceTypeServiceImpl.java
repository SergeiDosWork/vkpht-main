package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.WorkExperienceTypeService;
import me.goodt.vkpht.module.orgstructure.api.dto.WorkExperienceTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.WorkExperienceTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.WorkExperienceTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkExperienceTypeEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkExperienceTypeServiceImpl implements WorkExperienceTypeService {

    public static final String WORK_EXPERIENCE_TYPE_WITH_ID_D_IS_NOT_FOUND = "WorkExperienceType with id=%d is not found";
    private final WorkExperienceTypeDao workExperienceTypeDao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;

    @Override
    @Transactional(readOnly = true)
    public WorkExperienceTypeEntity findById(Long id) throws NotFoundException {
        WorkExperienceTypeEntity dbEntity = workExperienceTypeDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(WORK_EXPERIENCE_TYPE_WITH_ID_D_IS_NOT_FOUND, id)));
        unitAccessService.checkUnitAccess(dbEntity.getUnitCode());
        return dbEntity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkExperienceTypeEntity> findAll() {
        var filter = WorkExperienceTypeFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();

        return workExperienceTypeDao.find(filter, Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional
    public WorkExperienceTypeEntity create(WorkExperienceTypeDto dto) throws NotFoundException {
        WorkExperienceTypeEntity entity = new WorkExperienceTypeEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        Date currentDate = new Date();
        entity.setDateFrom(currentDate);
        entity.setUpdateDate(currentDate);
        Long employeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(employeeId);
        entity.setUpdateEmployeeId(employeeId);
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        return workExperienceTypeDao.save(entity);
    }

    @Override
    @Transactional
    public WorkExperienceTypeEntity update(WorkExperienceTypeDto dto) throws NotFoundException {
        WorkExperienceTypeEntity entity = findById(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUpdateDate(new Date());
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        return workExperienceTypeDao.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) throws NotFoundException {
        WorkExperienceTypeEntity entity = findById(id);
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        workExperienceTypeDao.save(entity);
    }
}
