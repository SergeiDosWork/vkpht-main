package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
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
