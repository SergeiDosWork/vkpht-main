package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTeamStatusEntity;

@Repository
public class ProjectTeamStatusDao extends AbstractArchivableDao<ProjectTeamStatusEntity, Long> {

    private static final QProjectTeamStatusEntity meta = QProjectTeamStatusEntity.projectTeamStatusEntity;

    public ProjectTeamStatusDao(EntityManager em) {
        super(ProjectTeamStatusEntity.class, em);
    }

    @Override
    protected JPQLQuery<ProjectTeamStatusEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
