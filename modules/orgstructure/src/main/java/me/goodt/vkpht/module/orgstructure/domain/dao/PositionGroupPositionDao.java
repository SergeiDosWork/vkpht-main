package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupPositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionGroupPositionEntity;

@Repository
public class PositionGroupPositionDao extends AbstractDao<PositionGroupPositionEntity, Long> {

    private static final QPositionGroupPositionEntity meta = QPositionGroupPositionEntity.positionGroupPositionEntity;

    public PositionGroupPositionDao(EntityManager em) {
        super(PositionGroupPositionEntity.class, em);
    }

    public List<PositionGroupPositionEntity> findAllByPositionIdAndPositionGroupId(Long positionId, Long positionGroupId) {
        final BooleanExpression exp = Expressions.allOf(
            meta.position.id.eq(positionGroupId),
            meta.positionGroup.id.eq(positionGroupId)
        );

        return query().selectFrom(meta)
            .where(exp)
            .fetch();
    }

    public List<PositionGroupPositionEntity> findAllByPositionId(Long positionId) {
        return query().selectFrom(meta)
            .where(meta.position.id.eq(positionId))
            .fetch();
    }

    public List<PositionGroupPositionEntity> findAllByPositionGroupId(Long positionGroupId) {
        return query().selectFrom(meta)
            .where(meta.positionGroup.id.eq(positionGroupId))
            .fetch();
    }
}
