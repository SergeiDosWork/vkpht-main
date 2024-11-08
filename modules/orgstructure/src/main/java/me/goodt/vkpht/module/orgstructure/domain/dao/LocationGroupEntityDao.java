package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.LocationGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QLocationGroupEntity;

@Repository
public class LocationGroupEntityDao extends AbstractArchivableDao<LocationGroupEntity, Long> {

    private static final QLocationGroupEntity meta = QLocationGroupEntity.locationGroupEntity;

    public LocationGroupEntityDao(EntityManager em) {
        super(LocationGroupEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    @Override
    protected JPQLQuery<LocationGroupEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }
}
