package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.api.LegalService;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityDto;
import me.goodt.vkpht.module.orgstructure.api.dto.projection.LegalEntityTeamAssignmentCompactProjection;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.LegalEntityTeamDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityEntity;
import me.goodt.vkpht.module.orgstructure.domain.factory.LegalEntityFactory;

@Service
@Transactional
@RequiredArgsConstructor
public class LegalServiceImpl implements LegalService {

    private final LegalEntityDao legalEntityDao;
    private final LegalEntityTeamDao legalEntityTeamDao;
    private final LegalEntityTeamAssignmentDao legalEntityTeamAssignmentDao;
    private final UnitAccessService unitAccessService;

    @Override
    @Transactional(readOnly = true)
    public List<LegalEntityDto> getLegalEntityList(List<Long> divisionIds, List<Long> divisionGroupIds) {
        return legalEntityDao.findByDivisionIdsAndDivisionGroupIds(divisionIds, divisionGroupIds, unitAccessService.getCurrentUnit())
                .stream()
                .map(LegalEntityFactory::create)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LegalEntityDto getLegalEntityDto(Long id) {
        return LegalEntityFactory.create(getLegalEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, List<Long>> getLegalEntityTeamIdList(Collection<Long> divisionTeamAssignmentIds) {
        return legalEntityTeamDao.findIdsByDtaId(divisionTeamAssignmentIds)
                .stream()
                .collect(Collectors.groupingBy(t -> t.get(0, Long.class), Collectors.mapping(t -> t.get(1, Long.class), Collectors.toList())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalEntityTeamAssignmentCompactProjection> getLegalEntityTeamAssignmentCompactByEmployeeId(Long employeeId) {
        return legalEntityTeamAssignmentDao.findCompactByEmployeeId(employeeId);
    }

    private LegalEntityEntity getLegalEntity(Long id) {
        LegalEntityEntity entity = legalEntityDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("legal_entity with id=%d is not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
