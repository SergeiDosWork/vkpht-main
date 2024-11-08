package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QLegalEntityStatusEntity;

@Repository
public class LegalEntityStatusDao extends AbstractArchivableDao<LegalEntityStatusEntity, Integer> {

    private static final QLegalEntityStatusEntity meta = QLegalEntityStatusEntity.legalEntityStatusEntity;

    public LegalEntityStatusDao(EntityManager em) {
        super(LegalEntityStatusEntity.class, em);
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
    protected JPQLQuery<LegalEntityStatusEntity> createActualQuery() {
        return query().selectFrom(meta)
            .where(meta.dateTo.isNull());
    }
}
