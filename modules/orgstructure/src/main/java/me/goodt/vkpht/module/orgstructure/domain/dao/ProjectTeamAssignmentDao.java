package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTeamAssignmentEntity;

@Repository
public class ProjectTeamAssignmentDao extends AbstractDao<ProjectTeamAssignmentEntity, Long> {

    private final static QProjectTeamAssignmentEntity meta = QProjectTeamAssignmentEntity.projectTeamAssignmentEntity;

    public ProjectTeamAssignmentDao(EntityManager em) {
        super(ProjectTeamAssignmentEntity.class, em);
    }

    public Long findIdByExternalId(String extId) {
        return query().select(meta.id)
                .from(meta)
                .where(meta.externalId.eq(extId))
                .fetchFirst();
    }
}
