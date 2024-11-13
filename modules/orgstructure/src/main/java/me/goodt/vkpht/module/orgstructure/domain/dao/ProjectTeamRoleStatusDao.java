package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamRoleStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTeamRoleStatusEntity;

@Repository
public class ProjectTeamRoleStatusDao extends AbstractArchivableDao<ProjectTeamRoleStatusEntity, Long> {

    private static final QProjectTeamRoleStatusEntity meta = QProjectTeamRoleStatusEntity.projectTeamRoleStatusEntity;

    public ProjectTeamRoleStatusDao(EntityManager em) {
        super(ProjectTeamRoleStatusEntity.class, em);
    }

    @Override
    protected JPQLQuery<ProjectTeamRoleStatusEntity> createActualQuery() {
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
