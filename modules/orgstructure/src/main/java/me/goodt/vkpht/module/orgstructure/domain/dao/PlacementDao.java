package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.PlacementEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPlacementEntity;

@Repository
public class PlacementDao extends AbstractDao<PlacementEntity, Long> {

    private static final QPlacementEntity meta = QPlacementEntity.placementEntity;

    public PlacementDao(EntityManager em) {
        super(PlacementEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
