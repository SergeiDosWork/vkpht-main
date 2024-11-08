package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.FunctionTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QFunctionTeamAssignmentEntity;

@Repository
public class FunctionTeamAssignmentDao extends AbstractDao<FunctionTeamAssignmentEntity, Long> {

	private static final QFunctionTeamAssignmentEntity meta = QFunctionTeamAssignmentEntity.functionTeamAssignmentEntity;

	public FunctionTeamAssignmentDao(EntityManager em) {
		super(FunctionTeamAssignmentEntity.class, em);
	}

	public Long findIdByExternalId(String externalId) {
		return query().from(meta)
			.select(meta.id)
			.where(meta.externalId.eq(externalId))
			.fetchFirst();
	}
}
