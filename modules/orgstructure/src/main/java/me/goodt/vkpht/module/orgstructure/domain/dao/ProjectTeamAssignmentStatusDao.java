package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamAssignmentStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTeamAssignmentStatusEntity;

@Repository
public class ProjectTeamAssignmentStatusDao extends AbstractArchivableDao<ProjectTeamAssignmentStatusEntity, Long> {

    private static final QProjectTeamAssignmentStatusEntity meta =
        QProjectTeamAssignmentStatusEntity.projectTeamAssignmentStatusEntity;

    public ProjectTeamAssignmentStatusDao(EntityManager em) {
        super(ProjectTeamAssignmentStatusEntity.class, em);
    }

    @Override
    protected JPQLQuery<ProjectTeamAssignmentStatusEntity> createActualQuery() {
        return query().selectFrom(meta)
            .where(meta.dateTo.isNull());
    }

    public Long findIdByExternalId(String extId) {
        return query().select(meta.id)
            .from(meta)
            .where(meta.externalId.eq(extId))
            .fetchFirst();
    }
}
