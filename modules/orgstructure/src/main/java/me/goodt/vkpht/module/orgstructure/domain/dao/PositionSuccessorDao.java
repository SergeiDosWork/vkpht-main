package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionSuccessorFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionSuccessorEntity;

@Repository
public class PositionSuccessorDao extends AbstractDao<PositionSuccessorEntity, Long> {
    private static final QPositionSuccessorEntity meta = QPositionSuccessorEntity.positionSuccessorEntity;

    public PositionSuccessorDao(EntityManager em) {
        super(PositionSuccessorEntity.class, em);
    }

    public List<PositionSuccessorEntity> findAll(PositionSuccessorFilter filter) {
        Predicate exp = toPredicate(filter);
        return query().selectFrom(meta)
            .where(exp)
            .fetch();
    }

    private Predicate toPredicate(PositionSuccessorFilter filter) {
        BooleanBuilder exp = new BooleanBuilder();
        if (filter.getEmployeeId() != null) {
            exp.and(meta.employeeId.eq(filter.getEmployeeId()));
        }
        if (filter.getActual() != null) {
            if (filter.getActual()) {
                exp.and(meta.dateTo.isNull());
            } else {
                exp.and(meta.dateTo.isNotNull());
            }
        }
        return exp;
    }
}
