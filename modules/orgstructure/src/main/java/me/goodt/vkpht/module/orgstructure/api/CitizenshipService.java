package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.CitizenshipInputDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipEntity;

public interface CitizenshipService {
    CitizenshipEntity getById(Long id) throws NotFoundException;

    List<CitizenshipEntity> getAllActual();

    CitizenshipEntity create(CitizenshipInputDto dto) throws NotFoundException;

    CitizenshipEntity update(CitizenshipInputDto dto, Long id) throws NotFoundException;

    void delete(Long id) throws NotFoundException;
}
