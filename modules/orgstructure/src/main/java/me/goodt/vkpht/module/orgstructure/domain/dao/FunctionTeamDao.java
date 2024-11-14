package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
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
