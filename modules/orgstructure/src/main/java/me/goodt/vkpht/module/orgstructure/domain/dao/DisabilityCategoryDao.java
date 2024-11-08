package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.DisabilityCategoryEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QDisabilityCategoryEntity;

@Repository
public class DisabilityCategoryDao extends AbstractDao<DisabilityCategoryEntity, Long> {
    private static final QDisabilityCategoryEntity meta = QDisabilityCategoryEntity.disabilityCategoryEntity;

    public DisabilityCategoryDao(EntityManager em) {
        super(DisabilityCategoryEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
