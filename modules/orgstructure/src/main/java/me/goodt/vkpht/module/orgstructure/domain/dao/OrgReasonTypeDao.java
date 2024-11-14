package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QOrgReasonTypeEntity;

@Repository
public class OrgReasonTypeDao extends AbstractDao<OrgReasonTypeEntity, Long> {

    private static final QOrgReasonTypeEntity meta = QOrgReasonTypeEntity.orgReasonTypeEntity;

    public OrgReasonTypeDao(EntityManager em) {
        super(OrgReasonTypeEntity.class, em);
    }

    public boolean existsByName(String name) {
        return query().selectFrom(meta)
            .where(meta.name.eq(name))
            .fetchCount() > 0;
    }
}
