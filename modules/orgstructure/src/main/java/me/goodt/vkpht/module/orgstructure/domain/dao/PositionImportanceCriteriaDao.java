package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceCriteriaEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionImportanceCriteriaEntity;

@Repository
public class PositionImportanceCriteriaDao extends AbstractDao<PositionImportanceCriteriaEntity, Long> {

    private static final QPositionImportanceCriteriaEntity meta = QPositionImportanceCriteriaEntity.positionImportanceCriteriaEntity;

    public PositionImportanceCriteriaDao(EntityManager em) {
        super(PositionImportanceCriteriaEntity.class, em);
    }

    public List<PositionImportanceCriteriaEntity> findAllByPositionIdAndImportanceCriteriaId(Long positionId, Long importanceCriteriaId) {
        BooleanExpression exp = meta.position.id.eq(positionId)
                .and(meta.importanceCriteria.id.eq(importanceCriteriaId))
                .and(meta.dateTo.isNull());
        return query().selectFrom(meta)
                .where(exp)
                .fetch();
    }
}
