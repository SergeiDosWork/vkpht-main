package me.goodt.vkpht.module.orgstructure.application;

import me.goodt.vkpht.module.orgstructure.api.ILibraryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentReadinessDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentRotationDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentRotationEntity;

/**
 * @author Pavel Khovaylo
 */
@Service
public class LibraryServiceImpl implements ILibraryService {

    @Autowired
    private AssignmentReadinessDao assignmentReadinessDao;
    @Autowired
    private AssignmentRotationDao assignmentRotationDao;
    @Autowired
    private UnitAccessService unitAccessService;

    @Override
    public AssignmentRotationEntity getAssignmentRotation(Integer id) throws NotFoundException {
        if (id == null)
            throw new NotFoundException("assignment rotation id is null");
        AssignmentRotationEntity entity = assignmentRotationDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("assignment rotation %d is not found", id)));
        unitAccessService.checkUnitAccess(unitAccessService.getCurrentUnit());
        return entity;
    }

    @Override
    public AssignmentReadinessEntity getAssignmentReadiness(Integer id) throws NotFoundException {
        if (id == null)
            throw new NotFoundException("assignment readiness id is null");
        AssignmentReadinessEntity entity = assignmentReadinessDao.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("assignment readiness %d is not found", id)));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        return entity;
    }
}
