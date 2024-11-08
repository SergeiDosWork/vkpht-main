package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.ReasonDto;
import me.goodt.vkpht.module.orgstructure.api.dto.ReasonTypeDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonTypeEntity;

public interface IReasonService {
    OrgReasonTypeEntity findReasonType(Long id) throws NotFoundException;

    List<OrgReasonTypeEntity> findReasonTypeAll();

    OrgReasonTypeEntity createReasonType(ReasonTypeDto dto);

    OrgReasonTypeEntity updateReasonType(ReasonTypeDto dto) throws NotFoundException;

	Integer deleteReasonType(Long id) throws NotFoundException;

    OrgReasonEntity findReason(Long id) throws NotFoundException;

    List<OrgReasonEntity> findReasonAll();

    OrgReasonEntity createReason(ReasonDto dto) throws NotFoundException;

    OrgReasonEntity updateReason(ReasonDto dto) throws NotFoundException;

    Long deleteReason(Long id) throws NotFoundException;
}
