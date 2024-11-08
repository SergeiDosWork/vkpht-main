package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.WorkExperienceTypeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkExperienceTypeEntity;

public interface IWorkExperienceTypeService {
    WorkExperienceTypeEntity findById(Long id) throws NotFoundException;

    List<WorkExperienceTypeEntity> findAll();

    WorkExperienceTypeEntity create(WorkExperienceTypeDto dto) throws NotFoundException;

    WorkExperienceTypeEntity update(WorkExperienceTypeDto dto) throws NotFoundException;

    void delete(Long id) throws NotFoundException;
}
