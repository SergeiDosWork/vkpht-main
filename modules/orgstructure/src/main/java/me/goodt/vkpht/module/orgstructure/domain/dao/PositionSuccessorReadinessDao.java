package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorReadinessEntity;

@Repository
public class PositionSuccessorReadinessDao extends AbstractDao<PositionSuccessorReadinessEntity, Long> {

    private static final QPositionSuccessorReadinessEntity meta = QPositionSuccessorReadinessEntity.positionSuccessorReadinessEntity;

    public PositionSuccessorReadinessDao(EntityManager em) {
        super(PositionSuccessorReadinessEntity.class, em);
    }

    // @Query(value = "SELECT * FROM org_position_successor_readiness WHERE
    // position_successor_id IN (:ids)", nativeQuery = true)
    public List<PositionSuccessorReadinessEntity> getByPositionSuccessorIds(List<Long> ids, String unitCode) {
        final QAssignmentReadinessEntity readiness = QAssignmentReadinessEntity.assignmentReadinessEntity;
        BooleanExpression exp = meta.positionSuccessorId.in(ids)
            .and(readiness.unitCode.eq(unitCode));
        return query().selectFrom(meta)
                .leftJoin(readiness).on(readiness.id.eq(meta.readiness.id))
                .where(exp)
                .fetch();
    }

    // @Query("select p from PositionSuccessorReadinessEntity p where
    // p.positionSuccessor.position.id = ?1")
    public List<PositionSuccessorReadinessEntity> findByPositionSuccessorPositionId(Long positionId, String unitCode) {
        final QAssignmentReadinessEntity readiness = QAssignmentReadinessEntity.assignmentReadinessEntity;
        final QPositionSuccessorEntity qps = QPositionSuccessorEntity.positionSuccessorEntity;
        BooleanExpression exp = qps.positionId.eq(positionId)
            .and(readiness.unitCode.eq(unitCode));
        return query().selectFrom(meta)
                .join(meta.positionSuccessor, qps)
                .leftJoin(readiness).on(readiness.id.eq(meta.readiness.id))
                .where(exp)
                .fetch();
    }

    public List<PositionSuccessorReadinessEntity> findAll(String unitCode) {
        final QAssignmentReadinessEntity readiness = QAssignmentReadinessEntity.assignmentReadinessEntity;
        BooleanExpression exp = readiness.unitCode.eq(unitCode);
        return query().selectFrom(meta)
            .leftJoin(readiness).on(readiness.id.eq(meta.readiness.id))
            .where(exp)
            .fetch();
    }
}
