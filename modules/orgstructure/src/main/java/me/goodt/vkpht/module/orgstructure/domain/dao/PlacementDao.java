package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
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
