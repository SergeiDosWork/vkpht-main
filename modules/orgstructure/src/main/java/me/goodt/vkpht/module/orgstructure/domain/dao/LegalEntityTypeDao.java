package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QLegalEntityTypeEntity;

@Repository
public class LegalEntityTypeDao extends AbstractArchivableDao<LegalEntityTypeEntity, Integer> {

    private static final QLegalEntityTypeEntity meta = QLegalEntityTypeEntity.legalEntityTypeEntity;

    public LegalEntityTypeDao(EntityManager em) {
        super(LegalEntityTypeEntity.class, em);
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
    protected JPQLQuery<LegalEntityTypeEntity> createActualQuery() {
        return query().selectFrom(meta)
            .where(meta.dateTo.isNull());
    }
}
