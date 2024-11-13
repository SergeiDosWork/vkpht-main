package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectStatusEntity;

@Repository
public class ProjectStatusDao extends AbstractArchivableDao<ProjectStatusEntity, Integer> {

    private static final QProjectStatusEntity meta = QProjectStatusEntity.projectStatusEntity;

    public ProjectStatusDao(EntityManager em) {
        super(ProjectStatusEntity.class, em);
    }

    public boolean existsByName(String name) {
        return query().selectFrom(meta)
            .where(meta.name.eq(name))
            .fetchCount() > 0;
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }

    @Override
    protected JPQLQuery<ProjectStatusEntity> createActualQuery() {
        return query().selectFrom(meta)
            .where(meta.dateTo.isNull());
    }
}
