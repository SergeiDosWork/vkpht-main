package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import me.goodt.vkpht.common.application.impl.AbstractArchiveDictionaryService;
import me.goodt.vkpht.module.orgstructure.domain.dao.ProjectTeamAssignmentStatusDao;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamAssignmentStatusEntity;
import me.goodt.vkpht.common.api.AuthService;

@Service
public class ProjectTeamAssignmentStatusCrudService extends
    AbstractArchiveDictionaryService<ProjectTeamAssignmentStatusEntity, Long> {

    @Getter
    @Autowired
    private ProjectTeamAssignmentStatusDao archivableDao;
    @Autowired
    private AuthService authService;

    @Override
    protected void afterCreate(ProjectTeamAssignmentStatusEntity entity) {
        Long sessionEmployeeId = authService.getUserEmployeeId();
        entity.setAuthorEmployeeId(sessionEmployeeId);
        entity.setUpdateEmployeeId(sessionEmployeeId);
    }

    @Override
    protected void afterUpdate(ProjectTeamAssignmentStatusEntity entity) {
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
    }

    @Override
    public void delete(Long id) {
        ProjectTeamAssignmentStatusEntity entity = archivableDao.findById(id).orElseThrow(() ->
            new NotFoundException(String.format("ProjectTeamAssignmentStatus with id = %s not found", id)));
        Date currentDate = new Date();
        entity.setDateTo(currentDate);
        entity.setUpdateDate(currentDate);
        entity.setUpdateEmployeeId(authService.getUserEmployeeId());
        archivableDao.save(entity);
    }
}
