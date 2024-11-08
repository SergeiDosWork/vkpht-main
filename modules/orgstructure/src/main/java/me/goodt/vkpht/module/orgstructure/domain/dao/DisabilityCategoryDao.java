package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
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
