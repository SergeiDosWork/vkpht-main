package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTypeEntity;

@Repository
public class ProjectTypeDao extends AbstractArchivableDao<ProjectTypeEntity, Integer> {

    private static final QProjectTypeEntity meta = QProjectTypeEntity.projectTypeEntity;

    public ProjectTypeDao(EntityManager em) {
        super(ProjectTypeEntity.class, em);
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
    protected JPQLQuery<ProjectTypeEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }
}
