package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentReadinessFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QAssignmentReadinessEntity;

@Repository
public class AssignmentReadinessDao extends AbstractDao<AssignmentReadinessEntity, Integer> {

    private static final QAssignmentReadinessEntity meta = QAssignmentReadinessEntity.assignmentReadinessEntity;

    public AssignmentReadinessDao(EntityManager em) {
        super(AssignmentReadinessEntity.class, em);
    }

    public Page<AssignmentReadinessEntity> find(AssignmentReadinessFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);
        return findAll(where, pageable);
    }

    private Predicate toPredicate(AssignmentReadinessFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }
        if (filter.getActual() != null) {
            if (filter.getActual()) {
                where.and(meta.dateTo.isNull());
            } else {
                where.and(meta.dateTo.isNotNull());
            }
        }
        return where;
    }
}
