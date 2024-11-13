package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTeamTypeEntity;

@Repository
public class ProjectTeamTypeDao extends AbstractArchivableDao<ProjectTeamTypeEntity, Long> {

    private static final QProjectTeamTypeEntity meta = QProjectTeamTypeEntity.projectTeamTypeEntity;

    public ProjectTeamTypeDao(EntityManager em) {
        super(ProjectTeamTypeEntity.class, em);
    }

    @Override
    protected JPQLQuery<ProjectTeamTypeEntity> createActualQuery() {
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
