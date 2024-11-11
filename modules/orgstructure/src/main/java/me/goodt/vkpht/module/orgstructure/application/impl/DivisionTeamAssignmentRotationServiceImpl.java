package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.module.orgstructure.api.DivisionTeamAssignmentRotationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentRotationDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentRotationEntity;

@Slf4j
@Service
public class DivisionTeamAssignmentRotationServiceImpl implements DivisionTeamAssignmentRotationService {

    @Autowired
    private DivisionTeamAssignmentRotationDao divisionTeamAssignmentRotationDao;

    @Override
    public DivisionTeamAssignmentRotationEntity getDivisionTeamAssignmentRotation(Long id) throws NotFoundException {
        if (id == null) {
            log.error("division team assignment rotation id is null");
            throw new NotFoundException("division team assignment rotation id is null");
        }
        Optional<DivisionTeamAssignmentRotationEntity> entity = divisionTeamAssignmentRotationDao.findById(id);
        if (entity.isEmpty()) {
            log.error("division team assignment rotation {} is not found", id);
            throw new NotFoundException(String.format("division team assignment rotation %d is not found", id));
        }
        return entity.get();
    }

    @Override
    public void updateDivisionTeamAssignmentRotationComment(Long id, String commentHr, String commentEmployee) throws NotFoundException {
        DivisionTeamAssignmentRotationEntity divisionTeamAssignmentRotation = getDivisionTeamAssignmentRotation(id);
        if (commentHr != null) {
            divisionTeamAssignmentRotation.setCommentHr(commentHr);
            divisionTeamAssignmentRotationDao.save(divisionTeamAssignmentRotation);
        }
        if (commentEmployee != null) {
            divisionTeamAssignmentRotation.setCommentEmployee(commentEmployee);
            divisionTeamAssignmentRotationDao.save(divisionTeamAssignmentRotation);
        }
    }
}
