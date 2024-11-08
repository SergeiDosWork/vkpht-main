package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamAssignmentTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTeamAssignmentTypeEntity;

@Repository
public class ProjectTeamAssignmentTypeDao extends AbstractArchivableDao<ProjectTeamAssignmentTypeEntity, Long> {

    private static final QProjectTeamAssignmentTypeEntity meta =
            QProjectTeamAssignmentTypeEntity.projectTeamAssignmentTypeEntity;

    public ProjectTeamAssignmentTypeDao(EntityManager em) {
        super(ProjectTeamAssignmentTypeEntity.class, em);
    }

    @Override
    protected JPQLQuery<ProjectTeamAssignmentTypeEntity> createActualQuery() {
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
