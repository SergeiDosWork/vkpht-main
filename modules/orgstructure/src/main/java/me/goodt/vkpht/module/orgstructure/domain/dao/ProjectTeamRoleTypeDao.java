package me.goodt.vkpht.module.orgstructure.domain.dao;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamRoleTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTeamRoleTypeEntity;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class ProjectTeamRoleTypeDao extends AbstractArchivableDao<ProjectTeamRoleTypeEntity, Long> {

    private static final QProjectTeamRoleTypeEntity meta = QProjectTeamRoleTypeEntity.projectTeamRoleTypeEntity;

    public ProjectTeamRoleTypeDao(EntityManager em) {
        super(ProjectTeamRoleTypeEntity.class, em);
    }

    @Override
    protected JPQLQuery<ProjectTeamRoleTypeEntity> createActualQuery() {
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
