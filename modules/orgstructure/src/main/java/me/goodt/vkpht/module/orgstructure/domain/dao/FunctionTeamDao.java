package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.FunctionTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QFunctionTeamEntity;

@Repository
public class FunctionTeamDao extends AbstractDao<FunctionTeamEntity, Long> {

    private static final QFunctionTeamEntity meta = QFunctionTeamEntity.functionTeamEntity;

    public FunctionTeamDao(EntityManager em) {
        super(FunctionTeamEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
