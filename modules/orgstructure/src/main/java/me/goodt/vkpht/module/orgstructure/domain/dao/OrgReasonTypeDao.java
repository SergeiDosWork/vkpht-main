package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

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
