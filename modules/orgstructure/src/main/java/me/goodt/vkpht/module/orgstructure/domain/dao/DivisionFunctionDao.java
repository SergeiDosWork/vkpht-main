package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionFunctionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QDivisionFunctionEntity;

@Repository
public class DivisionFunctionDao extends AbstractDao<DivisionFunctionEntity, Long> {

    private static final QDivisionFunctionEntity meta = QDivisionFunctionEntity.divisionFunctionEntity;

    public DivisionFunctionDao(EntityManager em) {
        super(DivisionFunctionEntity.class, em);
    }

    public DivisionFunctionEntity findByDivisionIdAndFunctionId(Long divisionId, Long functionId) {
        return query().selectFrom(meta)
            .where(meta.divisionId.eq(divisionId).and(meta.functionId.eq(functionId)))
            .fetchFirst();
    }
}
