package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionProfstandardInputDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionProfstandardEntity;

public interface PositionProfstandardService {
    List<PositionProfstandardEntity> getAll();
    PositionProfstandardEntity getById(Long id) throws NotFoundException;
    PositionProfstandardEntity create(PositionProfstandardInputDto dto) throws NotFoundException;
    PositionProfstandardEntity update(PositionProfstandardInputDto dto, Long id) throws NotFoundException;
    void delete(Long id) throws NotFoundException;
}
