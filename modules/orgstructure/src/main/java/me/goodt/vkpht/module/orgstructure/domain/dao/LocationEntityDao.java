package me.goodt.vkpht.module.orgstructure.domain.dao;

import me.goodt.vkpht.module.orgstructure.domain.entity.LocationEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QLocationEntity;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class LocationEntityDao extends AbstractDao<LocationEntity, Long> {

    private static final QLocationEntity meta = QLocationEntity.locationEntity;

    public LocationEntityDao(EntityManager em) {
        super(LocationEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
