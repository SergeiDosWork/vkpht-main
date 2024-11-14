package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QDivisionStatusEntity;

@Repository
public class DivisionStatusDao extends AbstractArchivableDao<DivisionStatusEntity, Integer> {

    private static final QDivisionStatusEntity meta = QDivisionStatusEntity.divisionStatusEntity;

    public DivisionStatusDao(EntityManager em) {
        super(DivisionStatusEntity.class, em);
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
    protected JPQLQuery<DivisionStatusEntity> createActualQuery() {
        return query().selectFrom(meta)
            .where(meta.dateTo.isNull());
    }
}
