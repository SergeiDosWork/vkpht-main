package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.FamilyStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QFamilyStatusEntity;

@Repository
public class FamilyStatusDao extends AbstractArchivableDao<FamilyStatusEntity, Integer> {

    private static final QFamilyStatusEntity meta = QFamilyStatusEntity.familyStatusEntity;

    public FamilyStatusDao(EntityManager em) {
        super(FamilyStatusEntity.class, em);
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
    protected JPQLQuery<FamilyStatusEntity> createActualQuery() {
        return query().selectFrom(meta)
            .where(meta.dateTo.isNull());
    }
}
