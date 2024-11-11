package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.WorkFunctionDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkFunctionEntity;

public interface WorkFunctionService {
    WorkFunctionEntity get(Long id) throws NotFoundException;

    List<WorkFunctionEntity> get();

    WorkFunctionEntity create(WorkFunctionDto dto) throws NotFoundException;

    WorkFunctionEntity update(Long id, WorkFunctionDto dto) throws NotFoundException;

    void delete(Long id) throws NotFoundException;
}
